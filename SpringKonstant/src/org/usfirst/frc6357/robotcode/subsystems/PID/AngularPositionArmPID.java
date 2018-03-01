package org.usfirst.frc6357.robotcode.subsystems.PID;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SpeedController;

public class AngularPositionArmPID implements PIDOutput
{
    private final SpeedController mySpeedController;
    private final PIDSource myDistanceMeasurment;
    private final PIDController myPidController;

    private final double Kp = 0;
    private final double Ki = 0;
    private final double Kd = 0;

    public AngularPositionArmPID(SpeedController inSpeedController, PIDSource inDistanceMeasurment)
    {
        mySpeedController = inSpeedController;
        myDistanceMeasurment = inDistanceMeasurment;

        // TODO: The pidOutput parameter (mySpeedController here) must be "this" because you
        // want the controller to call the local pidWrite function and not set the motor speed
        // directly. Setting the motors speed directly would bypass the limit switch test and
        // that wouldn't be a great idea. I suggest you pass in the ARM subsystem object and
        // use that in pidWrite(), calling the ARMSubsystem.periodic() function and passing
        // the speed parameter to pidWrite() to it.
        myPidController = new PIDController(Kp, Ki, Kd, myDistanceMeasurment, mySpeedController);
        myPidController.setOutputRange(-1, 1);
    }

    public void enable()
    {
        myPidController.enable();
    }

    public void disable()
    {
        myPidController.disable();
    }

    public void reset()
    {
        myPidController.reset();
        myPidController.enable();
    }

    public void setDistanceTarget(double distance)
    {
        myPidController.setSetpoint(distance);
    }

    @Override
    public void pidWrite(double output)
    {
        // TODO: Call the ARM periodic function here to set the motor controller speed
        // and also take into account the limit switches. This function is called by the
        // PIDController whenever it has a new motor speed calculated.

        // setDistanceTarget(output);
    }

    public double getPIDSetpoint()
    {
        return myPidController.getSetpoint();
    }

}
