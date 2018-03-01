package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

import org.usfirst.frc6357.robotcode.Ports;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Subsystem controlling the climb mechanism in the robot. This mechanism
 * comprises a single motor driving a winch, and two normally-open limit
 * switches which will be wired to the TalonSRX motor controller.
 */
public class ClimbSystem extends Subsystem
{
    private final WPI_TalonSRX climbMotor;

    public ClimbSystem()
    {
        climbMotor = new WPI_TalonSRX(Ports.ClimbWinchMotor);
        climbMotor.set(0.0);
    }

    public void setWinchSpeed(double speed)
    {
        climbMotor.set(speed);
    }

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
