package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.WriteBufferMode;

/**
 * This is a handler class for the TFMini LiDAR sensor for the FRC WPIlib
 * SerialPort class. The sensor returns packets of 9 bytes. Each byte has a
 * use.
 *
 * SENSOR INFORMATION
 *  ╔════════════╦═════════╗
 *  ║ Protocol   ║ UART    ║
 *  ╠════════════╬═════════╣
 *  ║ Baud Rate  ║ 115200  ║
 *  ╠════════════╬═════════╣
 *  ║ Data Bit   ║ 8       ║
 *  ╠════════════╬═════════╣
 *  ║ Stop bit   ║ 1       ║
 *  ╠════════════╬═════════╣
 *  ║ Parity bit ║ 0       ║
 *  ╚════════════╩═════════╝
 *
 *  DATA FORMAT of SERIAL PORT
 *  ╔════════╦═════════════╗
 *  ║ byte 1 ║ 0x59        ║
 *  ╠════════╬═════════════╣
 *  ║ byte 2 ║ 0x59        ║
 *  ╠════════╬═════════════╣
 *  ║ byte 3 ║ Dist_L      ║
 *  ╠════════╬═════════════╣
 *  ║ byte 4 ║ Dist_H      ║
 *  ╠════════╬═════════════╣
 *  ║ byte 5 ║ Strength_L  ║
 *  ╠════════╬═════════════╣
 *  ║ byte 6 ║ Strength_H  ║
 *  ╠════════╬═════════════╣
 *  ║ byte 7 ║ Reserved    ║
 *  ╠════════╬═════════════╣
 *  ║ byte 8 ║ Raw Quality ║
 *  ╠════════╬═════════════╣
 *  ║ byte 9 ║ Check Sum   ║
 *  ╚════════╩═════════════╝
 *
 * @author hoo761
 */

public class TFMini extends Thread
{
    private final SerialPort sensor;        // WPI serial port class

    // Serial port information
    private final int PACKET_LENGTH = 9;    // Length of the Packets/Data that the sensor sends back
    private final byte startByte = 0x59;    // This byte is the start of a UART message, also == to the double 89 (byte 1, and 2)
    private final int baudRate = 115200;    // The baud rate to configure the serial port
    private final int dataBits = 8;         // The number of data bits per transfer

    // Data bytes
    private int distance;    //Distance being read
    private int strength;    //Strength of signal
    private int quality;     //Quality of signal
    private int checksum;    // byte 9: checksum

    private boolean startRead = true;              // Boolean to start or stop reading values
    public ReceivingState state;            // State of the packet receive
    public byte[] packet = new byte[9];     // Buffer for one packet
    private int count = 0;

    private static TFMini m_instance = null;

    /**
     * Constructor, creates an instance of Serial Port class.
     * @param - SerialPort.Port enum, pick where it is plugged into
     */
    protected TFMini()
    {
        sensor = new SerialPort(baudRate, SerialPort.Port.kMXP, dataBits, SerialPort.Parity.kNone, SerialPort.StopBits.kOne);
        state = ReceivingState.WAIT_START1;
        initialize();
    }

    public static TFMini getInstance(){
        if(m_instance == null) 
        {
            m_instance = new TFMini();
        }
        return m_instance;
    }

    private enum ReceivingState
    {
        WAIT_START1,
        WAIT_START2,
        RECEIVING,
    }

    /**
     * Stops the reading of the LiDAR
     */
    public void stopLidar()
    {
        startRead = false;
    }

    /**
     * Returns the distance. By By multiplying distH by 256, the binary data is
     * shifted by 8 to the left. Now the lower 8-bit distance data, distL is added
     * resulting in 16-bit data of total distance.
     * @return - the total distance in centimeters
     */
    public int getDistance()
    {
        return distance;
    }

    /**
     * Returns the strength of the signal
     * @return strength
     */
    public int getStrength()
    {
        return strength;
    }

    /**
     * Gets the signal quality degree byte as a double
     * @return - signal quality degree byte
     */
    public int getSignalQualityDegree()
    {
        return quality;
    }

    /**
     * This forwards the byte list of a configuration command that is
     * required to receive data from it (part of the initialization process).
     * Also sets the timeout to be 4 packet times.
     */
    private void initialize()
    {
        //sensor.write(config, config.length);
        sensor.setReadBufferSize(PACKET_LENGTH + 3);
        sensor.setWriteBufferMode(WriteBufferMode.kFlushWhenFull);
        sensor.setTimeout(4/100);
    }

    /**
     * Runs a loop in a Thread that will read the 9 byte packets and set each
     * byte to the correct piece of data for use if the data is valid.
     */
    public void run() 
    {
        startRead = true;
        try 
        {
            while (startRead) 
            {
                byte bytes[] = sensor.read(1);
                if (bytes.length > 0) {
                    switch (state) 
                    {
                        case WAIT_START1:
                            if (bytes[0] == startByte) 
                            {
                                // We've read our first 0x59 (packet start marker) so set things up just in case
                                // this really is the start of a packet. Clear our byte counter, initialize the
                                // checksum and store the first byte.
                                count = 0;
                                checksum = bytes[0];
                                packet[count] = bytes[0];

                                // Start looking for the next character which we hope is another 0x59.
                                state = ReceivingState.WAIT_START2;
                            }
                            break;
                        case WAIT_START2:
                            if (bytes[0] == startByte) 
                            {
                                // We received a second 0x59 so this really looks like a new packet.
                                // Store the byte, update the checksum and byte counter then start reading
                                // bytes until we reach the end of the packet.
                                count++;
                                checksum += bytes[0];
                                packet[count] = bytes[0];
                                state = ReceivingState.RECEIVING;
                            } 
                            else 
                            {
                                // This wasn't an 0x59 so we are not at the start of a packet. Go back and
                                // start looking for a new packet header.
                                state = ReceivingState.WAIT_START1;
                            }
                            break;
                        case RECEIVING:
                            // Save this byte in our packet buffer.
                            count++;
                            packet[count] = bytes[0];

                            // Have we read a whole packet of data?
                            if (count == PACKET_LENGTH - 1) 
                            {
                                //System.out.println("Packet Complete");
                                // Reached the end of the packet, so the byte we just received is the
                                // checksum. Make sure it agrees with the checksum we've been accumulating
                                // as we read packet bytes.
                                if ((checksum & 0xFF) == (bytes[0] & 0xFF)) 
                                {
                                    //System.out.println("Checksum Passed");
                                    // Checksum is good so extract the distance, strength and quality info from
                                    // the received packet.
                                    parsePacket();
                                    sensor.flush();
                                    Thread.sleep(10);
                                }
                                else {
                                    //System.out.println("Checksum Failed");
                                }

                                // Start looking for the start of the next packet.
                                state = ReceivingState.WAIT_START1;
                            } 
                            else 
                            {
                                // We're not at the end of the packet so just update our checksum
                                // accumulator and keep receiving.
                                checksum += bytes[0];
                            }
                            break;
                    }
                }
            }
        } 
        catch (Exception e) {}
    }

    public void parsePacket()
    {
        synchronized(this) 
        {
            distance = ((((int)(packet[3] & 0xFF)) * 256) + ((int)(packet[2] & 0xFF)));
            strength = ((((int)(packet[5] & 0xFF)) * 256) + ((int)(packet[4] & 0xFF)));
            quality = (int)(packet[7] & 0xFF);
        }
    }
}
