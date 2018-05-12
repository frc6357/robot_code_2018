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
    private final SerialPort.Parity parity = SerialPort.Parity.kEven;
    
    // This is a list of bytes for the initialization of the LiDAR
    private final byte[] config = {0x42, 0x57, 0x02, 0x00, 0x00, 0x00, 0x01, 0x06};
    
    // Data bytes
    private double distL;					// byte 3: lower 8bits of distance
    private double distH;					// byte 4: higher 8bits of distance
    private double strengthL;				// byte 5: lower 8bit of strength
    private double strengthH; 				// byte 6: higher 8bit of strength
    private double reservedBytes;			// byte 7: reserved bytes
    private double signalQualityDegree;		// byte 8: original signal quality degree
    
    private boolean startRead;		// Boolean to start or stop reading values
    
    /**
     * Constructor, creates an instance of Serial Port class.
     * @param - SerialPort.Port enum, pick where it is plugged into
     */
    public TFMini(SerialPort.Port port)
    {
        sensor = new SerialPort(baudRate, port, dataBits, parity, stopBits);
        initilize();
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
     * @return - the total distance
     */
    public double getDistance()
    {
    	return (((int) distH * 256) + ((int) distL));
    }
    
    /**
     * Gets the distance lower byte as a double
     * @return - distance lower byte
     */
    public double getDistL()
    {
    	return distL;
    }
    
    /**
     * Gets the distance higher byte as a double
     * @return - distance higher byte
     */
    public double getDistH()
    {
    	return distH;
    }
    
    /**
     * Gets the strength higher byte as a double
     * @return - strength higher byte
     */
    public double getStrengthH()
    {
    	return strengthH;
    }
    
    /**
     * Gets the strength lower byte as a double
     * @return - strength lower byte
     */
    public double getStrengthL()
    {
    	return strengthL;
    }
    
    /**
     * Gets the reserved byte as a double
     * @return - reserved byte
     */
    public double getReservedBytes()
    {
    	return reservedBytes;
    }
    
    /**
     * Gets the signal quality degree byte as a double
     * @return - signal quality degree byte
     */
    public double getSignalQualityDegree()
    {
    	return signalQualityDegree;
    }
    
    /**
     * This forwards the byte list of a configuration command that is 
     * required to receive data from it (part of the initialization process).
     * Also sets the timeout.
     */
    private void initilize()
    {
    	sensor.write(config, config.length);
    	sensor.setTimeout(1/100);				// Sensor is 100Hz, so timeout is .001 seconds
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
     * Read raw bytes out of the buffer
     * @return An array of the read bytes
     */
    private byte[] readBytes()
    {
        return sensor.read(PACKET_LENGTH);
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
            	   if(amountOfBytes() >= 9)
            	   {
            		   byte bytes[] = readBytes();
            		   if(isValid(bytes))
            		   {
            			   distL = bytes[2];
            			   distH = bytes[3];
            			   strengthL = bytes[4];
            			   strengthH = bytes[5];
            			   reservedBytes = bytes[6];
            			   signalQualityDegree = bytes[7];
            		   }
            	   }
               }
            }
            catch(Exception e) {}
        }).start();
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
