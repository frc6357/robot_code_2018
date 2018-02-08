package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc6357.robotcode.Ports;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Subsystem controlling the power cube intake mechanism in the robot. This mechanism comprises
 * two motors connected to TalonSRX motor controllers and a pneumatic actuator to swing the
 * mechanism upwards into its stowed position. Current limits are set on the motors to cause them
 * to stop automatically when blocked.
 */
public class IntakeSystem extends Subsystem {

    public final WPI_TalonSRX intakeLeftMotor;
    public final WPI_TalonSRX intakeRightMotor;

    public IntakeSystem()
    {
        intakeLeftMotor  = new WPI_TalonSRX(Ports.ArmElevationMotor);
        intakeRightMotor = new WPI_TalonSRX(Ports.ArmElevationMotor);

        intakeLeftMotor.configPeakCurrentLimit(35, 10);       /* 35 A */
        intakeLeftMotor.configPeakCurrentDuration(200, 10);   /* 200ms */
        intakeLeftMotor.configContinuousCurrentLimit(30, 10); /* 30A */
        intakeLeftMotor.enableCurrentLimit(true);

        intakeRightMotor.configPeakCurrentLimit(35, 10);       /* 35 A */
        intakeRightMotor.configPeakCurrentDuration(200, 10);   /* 200ms */
        intakeRightMotor.configContinuousCurrentLimit(30, 10); /* 30A */
        intakeRightMotor.enableCurrentLimit(true);

        intakeRightMotor.setInverted(true);
        intakeRightMotor.set(ControlMode.Follower, intakeLeftMotor.getDeviceID());

        intakeLeftMotor.set(0.0);
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void setIntakeSpeed(double speed)
    {
        intakeLeftMotor.set(speed);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

