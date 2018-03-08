package org.usfirst.frc6357.robotcode.subsystems.PID;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SpeedController;

/**
 *  This class is the PID controller for velocity control.
 */
public class VelocityControlledDrive implements PIDOutput
{
    
    private final SpeedController mySpeedController;
    private final PIDSource mySpeedMeasurement;
    private final PIDController myPidController;

    private final double Kp = 0.1;
    private final double Kd = 0.0;
    private final double Ki = 0.0;

    private final double maxRobotSpeed = 9.5; // TODO set the feet per second
    
    /**
     * Constructor
     * 
     * @param inSpeedController - The speed controller
     * @param inSpeedMeasurement - The PID encoder source for speed measurement
     */
    public VelocityControlledDrive(SpeedController inSpeedController, PIDSource inSpeedMeasurement)
    {
        mySpeedController = inSpeedController;
        mySpeedMeasurement = inSpeedMeasurement;
        myPidController = new PIDController(Kp, Ki, Kd, mySpeedMeasurement, mySpeedController);
        
        myPidController.setOutputRange(-1.0, 1.0);
    }
    
    /**
     * Enables the PID
     */
    public void enable()
    {
        myPidController.enable();
    }
    
    /**
     * Disables the PID
     */
    public void disable()
    {
        myPidController.disable();
    }
    
    /**
     * Sets the absolute speed in feet per second
     * 
     * @param speed - Speed in feet per second
     */
    public void setSpeedAbsoluteFps(double speed)
    {
        myPidController.setSetpoint(speed);
    }
    
    /**
     * Sets the speed set point based off a percent of the max robot speed
     * 
     * TODO Set the max robot speed
     * 
     * @param percent
     */
    public void setSpeedPercent(double percent)
    {
        myPidController.setSetpoint(percent * maxRobotSpeed);
    }
    
    /**
     * Resets the PID controller
     */
    public void reset()
    {
        myPidController.reset();
        myPidController.enable();
    }
    
    /**
     * Writes to the PID
     */
    public void pidWrite(double output)
    {
        mySpeedController.set(output);
    }
    
    /**
     * 
     * @return returns the current set setpoint
     */
    public double getSetpoint()
    {
        return myPidController.getSetpoint();
    }
}

