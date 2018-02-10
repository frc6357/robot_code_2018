package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;

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

    private static final double INTAKE_SPEED = 0.25;
    public final WPI_TalonSRX intakeLeftMotor;
    public final WPI_TalonSRX intakeRightMotor;
    public final Solenoid intakeSolenoid;
    public boolean intakeIsUp = true;

    public IntakeSystem()
    {
        intakeSolenoid   = new Solenoid(Ports.IntakeTiltSolenoid);

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

    /**
     * Helper function used to control the state of both intake motors at the
     * same time. This allows the motors to be started and run in forwards or
     * backwards direction, or stopped. When started, motors are run at a constant
     * speed.
     *
     * @param Start   - if true, the intake motors are started. If false, they are stopped.
     * @param Inwards - if Start is true, this controls the motor direction, inwards (true) or
     *                  outwards (false).
     */
    public void setIntakeMotorState(boolean Start, boolean Inwards)
    {
        double Speed;

        if(!Start)
        {
            Speed = 0.0;
        }
        else
        {
            Speed = INTAKE_SPEED;

            if(Inwards)
            {
                Speed *= -1;
            }
        }

        setIntakeSpeed(Speed);
    }

    public boolean toggleIntakeSwing()
    {
        if(intakeIsUp)
        {
            setIntakeDown();
        }
        else
        {
            setIntakeDown();
        }

        return(intakeIsUp);
    }

    public void setIntakeUp()
    {
        // TODO: Check polarity of this control.
        intakeSolenoid.set(true);
        intakeIsUp = true;
    }

    public void setIntakeDown()
    {
        // TODO: Check polarity of this control.
        intakeSolenoid.set(false);
        intakeIsUp = false;
    }

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
