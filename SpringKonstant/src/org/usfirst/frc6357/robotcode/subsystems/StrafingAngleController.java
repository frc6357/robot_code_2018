package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * This class implements a PID controller which will read the robot angle
 * from the IMU passed to the constructor and return, via getSpeedAdjust(),
 * the amount to adjust the main drive motor speeds to turn the robot
 * until it is pointing to the current setpoint angle. This is intended
 * primarily for use while strafing to counteract the rotational effect of
 * having the strafing wheel offset from the robot's center of gravity.
 *
 * Typical use:
 *
 * 1. Create the object.
 * 2. When starting to strafe, call getCurrentAngle() and then use this
 *    value as the parameter to setAngleSetpoint() (to keep the robot
 *    pointing the same direction while strafing). Start the controller by
 *    calling enable().
 * 3. Periodically call getSpeedAdjust() to return the L/R speed differential
 *    to apply to adjust for rotation. Apply half of this adjustment to the
 *    left drive and negative half to the right drive.
 * 4. When strafing ends, call disable().
 */
public class StrafingAngleController extends Subsystem implements PIDSource, PIDOutput
{
    static final double kP = 0.01;
    static final double kI = 0.00;
    static final double kD = 0.00;
    static final double kF = 0.00;
    static final double toleranceDegrees = 2.0;
    private final IMU   imuInput;
    private final PIDController PID;
    private double speedAdjust;

    // Initialize your subsystem here
    public StrafingAngleController(IMU imu)
    {
        imuInput         = imu;
        speedAdjust  = 0.0;

        PID = new PIDController(kP, kI, kD, kF, this, this);
        PID.setInputRange(0.0f,  360.0f);
        PID.setOutputRange(-1.0, 1.0);
        PID.setAbsoluteTolerance(toleranceDegrees);
        PID.setContinuous(true);
        PID.disable();
    }

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    // General public methods
    public void enable()
    {
        PID.enable();
    }

    public void disable()
    {
        PID.disable();
    }

    public void setAngleSetpoint(double angle)
    {
        PID.setSetpoint(angle);
    }

    public double getCurrentAngle()
    {
        return imuInput.getAngle();
    }

    public double getSpeedAdjust()
    {
        return speedAdjust;
    }

    // PIDSource Methods
    public double pidGet()
    {
        return imuInput.getAngle();
    }

    public PIDSourceType getPIDSourceType()
    {
        return PIDSourceType.kDisplacement;
    }

    public void setPIDSourceType(PIDSourceType pid_type)
    {
        // Our PID type is fixed at kDisplacement.
    }

    // PIDOutput Methods
    public void pidWrite(double output)
    {
        speedAdjust  = output;
    }
}
