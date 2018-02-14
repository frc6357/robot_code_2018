package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc6357.robotcode.Ports;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
/**
 * Subsystem controlling the power cube intake mechanism in the robot. This mechanism comprises
 * a single motor connected to a TalonSRX motor controller. Running the motor changes the angle of
 * the arm. We set current limits on the arm to stop the motors if the arm becomes blocked.
 *
 * TODO: If we have an encoder on the arm, we should update this module to use position control.
 *       Currently, it merely sets the motor speed and leaves the operator to decide when to start
 *       and stop the motor.
 *
 * TODO: Set the current limit parameters sensibly.
 */
public class ArmSystem extends Subsystem
{
    private final WPI_TalonSRX armMotor;

    public ArmSystem()
    {
        armMotor = new WPI_TalonSRX(Ports.ArmElevationMotor);
        /*
        armMotor.set(0.0);
        armMotor.configPeakCurrentLimit(35, 10);       // 35 A
        armMotor.configPeakCurrentDuration(200, 10);   // 200ms
        armMotor.configContinuousCurrentLimit(30, 10); // 30A
        armMotor.enableCurrentLimit(true);
        */
    }

    public void setArmSpeed(double speed)
    {
        armMotor.set(speed);
    }

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
