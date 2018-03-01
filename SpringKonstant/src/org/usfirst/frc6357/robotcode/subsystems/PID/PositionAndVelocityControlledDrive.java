package org.usfirst.frc6357.robotcode.subsystems.PID;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;

/**
 * This class handles the PID controller for velocity and for the position
 * control of the robot. There are two PID controllers. One for position and
 * distance, the other is for velocity control for the speed controllers. This
 * class uses both PID controllers for distance and speed control.
 * 
 * The position and velocity PID controller is for autonomous, and the velocity
 * PID controller is for teleop.
 * 
 */
public class PositionAndVelocityControlledDrive implements PIDOutput
{
    private final SpeedController mySpeedController;
    private final Encoder myEncoder;
    private final PIDController myPidController;
    private final VelocityControlledDrive myVelocityControl;

    private final double Kp = 2.0;
    private final double Kd = 0.1;
    private final double Ki = 0.0;

    private boolean isPositionModeEnabled;

    /**
     * Constructor
     * 
     * @param inSpeedController
     *            - The speed controller
     * @param inEncoder
     *            - The Encoder
     */
    public PositionAndVelocityControlledDrive(SpeedController inSpeedController, Encoder inEncoder)
    {
        mySpeedController = inSpeedController;
        myEncoder = inEncoder;
        myVelocityControl = new VelocityControlledDrive(mySpeedController, new EncoderSpeedDrivePID(inEncoder));

        myPidController = new PIDController(Kp, Ki, Kd, new EncoderPositionDrivePID(myEncoder), myVelocityControl);

        myPidController.setOutputRange(-1.0, 1.0);
        myPidController.disable();

        isPositionModeEnabled = false;

    }

    /**
     * Enables the PID controller
     */
    public void enable()
    {
        myVelocityControl.enable();
        if (isPositionModeEnabled)
        {
            myPidController.enable();
        }
    }

    /**
     * Disables the PID controller
     */
    public void disable()
    {
        myVelocityControl.disable();
        myPidController.disable();
    }

    /*
     * Sets a distance as the target set point for the PID
     */
    public boolean setDistanceTarget(double distance)
    {
        if (isPositionModeEnabled)
        {
            myPidController.setSetpoint(distance);
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Sets the mode to position and velocity mode
     */
    public void setPositionMode()
    {
        myPidController.reset();
        myVelocityControl.reset();
        myPidController.enable();
        isPositionModeEnabled = true;
    }

    /**
     * Sets the mode to velocity mode
     */
    public void setVelocityMode()
    {
        myVelocityControl.reset();
        myPidController.disable();
        isPositionModeEnabled = false;
    }

    /**
     * Sets the target speed for the PID
     * 
     * @param speed
     *            - Speed in feet per second
     * @return returns false if PID not enabled
     */
    public boolean setSpeedAbsolute(double speed)
    {
        if (!isPositionModeEnabled)
        {
            myVelocityControl.setSpeedAbsoluteFps(speed);
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Sets the speed based on a percent
     * 
     * @param percent
     *            - percent of speed
     * @return returns false if position mode is enabled
     */
    public boolean setSpeedPercent(double percent)
    {
        if (!isPositionModeEnabled)
        {
            myVelocityControl.setSpeedPercent(percent);
            return true;
        } else
        {
            return false;
        }
    }
    
    /**
     * @return returns the current set point
     */
    public double getSpeedSetPoint()
    {
        return myVelocityControl.getSetpoint();
    }

    @Override
    public void pidWrite(double output)
    {
        setDistanceTarget(output);
    }

}
