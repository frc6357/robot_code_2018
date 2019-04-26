package frc.robot.subsystems;

import frc.robot.Ports;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Subsystem;

public class SensorSystem extends Subsystem
{
    Ultrasonic leftSonic;
    Ultrasonic rightSonic;
    
    /**
     * Default Constructor
     * Instantiates all of the sensors and makes the sensors automatically ping
     */
    public SensorSystem()
    {
        leftSonic = new Ultrasonic(Ports.leftUltrasonicInput, Ports.leftUltrasonicOutput);
        rightSonic = new Ultrasonic(Ports.rightUltrasonicInput, Ports.rightUltrasonicOutput);
        leftSonic.setEnabled(true);
        rightSonic.setEnabled(true);
        leftSonic.setAutomaticMode(true);
        rightSonic.setAutomaticMode(true);
    }
    
    /**
     * Method to get the distance in inches of the ping of
     * the sensor towards the center of the field
     * @return distance given by ping towards the center of the field in inches
     */
    public double getDistanceToCenter()
    {
        if(Robot.chooserStart.getSelected().equals("L"))
            return leftSonic.getRangeInches();
        return rightSonic.getRangeInches();
    }
    
    @Override
    protected void initDefaultCommand()
    {
        // TODO Auto-generated method stub
        
    }
}
