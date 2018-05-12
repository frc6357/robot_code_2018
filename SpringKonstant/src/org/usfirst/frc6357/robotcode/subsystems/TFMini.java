package org.usfirst.frc6357.robotcode.subsystems;

import org.usfirst.frc6357.robotcode.Robot;

import edu.wpi.first.wpilibj.SerialPort;

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

public class TFMini
{
    private final SerialPort sensor;		// WPI serial port class
    
    // Serial port information
    private final int PACKET_LENGTH = 9;	// Length of the Packets/Data that the sensor sends back
    private final byte startByte = 0x59;	// This byte is the start of a UART message, also == to the double 89 (byte 1, and 2)
    private final int baudRate = 115200;	// The baud rate to configure the serial port
    private final int dataBits = 8;			// The number of data bits per transfer
    private final SerialPort.StopBits stopBits = SerialPort.StopBits.kOne;
    private final SerialPort.Parity parity = SerialPort.Parity.kNone;
    private final SerialPort.Port port = SerialPort.Port.kOnboard; //Port used to plug in TFMini
    
    // This is a list of bytes for the initialization of the LiDAR
    private final byte[] config = {0x42, 0x57, 0x02, 0x00, 0x00, 0x00, 0x01, 0x06};
    
    // Data bytes
    private int distance;    //Distance being read
    private int strength;    //Strength of signal
    private int quality;     //Quality of signal
    private int checksum;    // byte 9: checksum
    
    private boolean startRead;		// Boolean to start or stop reading values
    private ReceivingState state;   // State of the packet receive
    private byte[] packet = new byte[9];    // Buffer for one packet
    private int count = 0;
    
    /**
     * Constructor, creates an instance of Serial Port class.
     * @param - SerialPort.Port enum, pick where it is plugged into
     */
    public TFMini()
    {
        sensor = new SerialPort(baudRate, port, dataBits, parity, stopBits);
        state = ReceivingState.WAIT_START1;
        initilize();
    }
    
    private enum ReceivingState
    {
        WAIT_START1,
        WAIT_START2,
        RECEIVING,
    }
    
    /**
     * Starts the reading of numbers from the LiDAR
     */
    public void start()
    {
    	startRead = true;
    	runLiDAR();
    }
    
    /**
     * Stops the reading of the LiDAR
     */
    public void stop()
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
     * Also sets the timeout.
     */
    private void initilize()
    {
    	sensor.write(config, config.length);
    	sensor.setReadBufferSize(PACKET_LENGTH);
    	sensor.setTimeout(4/100);
    }
    
    /**
     * Gets the number of bytes currently available to read from the serial port
     * @return - The number of bytes available to read
     */
    private int amountOfBytes()
    {
        return sensor.getBytesReceived();
    }
    
    /**
     * Runs a loop in a Thread that will read the 9 byte packets and set each
     * byte to the correct piece of data for use if the data is valid.
     */
    private void runLiDAR()
    {
    	new Thread(() ->
        {
            try
            {
               while(startRead)
               {
                   byte bytes[] = sensor.read(1);
                   if(bytes.length > 0)
                   {
                       switch(state)
                       {
                           case WAIT_START1:
                               if(bytes[0] == 0x59)
                               {
                                   count = 0;
                                   checksum = bytes[0];
                                   packet[0] = bytes[0];
                                   state = ReceivingState.WAIT_START2;
                               }
                               break;
                           case WAIT_START2:
                               if(bytes[0] == 0x59)
                               {
                                   count++;
                                   checksum += bytes[0];
                                   packet[1] = bytes[0];
                                   state = ReceivingState.RECEIVING;
                               }
                               else
                               {
                                   state = ReceivingState.WAIT_START1;
                               }
                               break;
                           case RECEIVING:
                               count++;
                               packet[count] = bytes[0];
                               if(count == PACKET_LENGTH - 1)
                               {
                                   // Reached the end of the packet, check checksum
                                   if(checksum == bytes[0])
                                   {
                                       parsePacket();
                                   }
                                   state = ReceivingState.WAIT_START1;
                               }
                               else
                               {
                                   checksum += bytes[0];
                               }
                               break;
                       }
                   }
               }
            }
            catch(Exception e) {}
        }).start();
    }
    
    public void parsePacket()
    {
        distance = (((int) packet[3] * 256) + ((int) packet[2]));
        strength = (((int) packet[5] * 256) + ((int) packet[4]));
        quality = (int)packet[7];
    }
    
    /**
     * Checks if the packet of 9 bytes is a data packet and if so, has to pass
     * the checksum.
     * @param bytes - array of bytes
     * @return - true if starting two bytes are equal to 0x59 (the starting bytes)
     * and passes the checksum.
     */
    private boolean isValid(byte[] bytes)
    {
    	return bytes[0] == startByte 
    			&& bytes[1] == startByte 
    			&& checkChecksum(bytes, bytes[9]);
    }
    
    /**
     * The checksum is the sum of byte1 - byte8. This checks if the checksum
     * is equal to the sum of the bytes.
     * @param bytes - array of bytes
     * @param checkSum - the checksum byte
     * @return true if checksum is equal to the sum of previous bytes
     */
    private boolean checkChecksum(byte[] bytes, byte checkSum)
    {
    	double total = 0;
    	
    	for(int i = 0; i < dataBits; i++)
    	{
    		total += (double) bytes[i];
    	}
    	return total == (double) checkSum;
    }
}
