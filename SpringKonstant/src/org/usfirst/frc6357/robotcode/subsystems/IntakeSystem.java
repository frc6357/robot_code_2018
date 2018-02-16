package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import org.usfirst.frc6357.robotcode.Ports;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * Subsystem controlling the power cube intake mechanism in the robot. This
 * mechanism comprises two motors connected to TalonSRX motor controllers and a
 * pneumatic actuator to swing the mechanism upwards into its stowed position.
 * Current limits are set on the motors to cause them to stop automatically when
 * blocked.
 */
public class IntakeSystem extends Subsystem
{

    private static final double INTAKE_SPEED = 0.25;
    public final WPI_TalonSRX intakeLeftMotor;
    public final WPI_TalonSRX intakeRightMotor;
    public final DoubleSolenoid intakeSolenoid;
    public boolean intakeIsUp = true;

    public IntakeSystem()
    {
        // TODO: Update if we add solenoids to control intake grippers.

        intakeSolenoid = new DoubleSolenoid(Ports.PCM_ID, Ports.IntakeTiltSolenoidUp, Ports.IntakeTiltSolenoidDown);

        intakeLeftMotor = new WPI_TalonSRX(Ports.ArmElevationMotor);
        intakeRightMotor = new WPI_TalonSRX(Ports.ArmElevationMotor);

        // TODO: Set current limits sensibly so that we catch case where a cube is
        // pulled into the mechanism and the motor stalls.

        /*
         * intakeRightMotor.configPeakCurrentLimit(35, 10); // 35 A
         * intakeRightMotor.configPeakCurrentDuration(200, 10); // 200ms
         * intakeRightMotor.configContinuousCurrentLimit(30, 10); // 30A
         * intakeRightMotor.enableCurrentLimit(true);
         * 
         * intakeRightMotor.setInverted(true);
         * intakeRightMotor.set(ControlMode.Follower, intakeLeftMotor.getDeviceID());
         * 
         * intakeLeftMotor.set(0.0);
         */
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    /**
     * Sets the raw speed of the intake motors.
     *
     * @param speed
     *            The speed setting for the intake motors. Valid values are in the
     *            range -1.0 to 1.0.
     *
     * @return None
     */
    public void setIntakeSpeed(double speed)
    {
        intakeLeftMotor.set(speed);

        SmartDashboard.putNumber("Intake speed", speed);
        SmartDashboard.putNumber("Intake left current",  intakeLeftMotor.getOutputCurrent());
        SmartDashboard.putNumber("Intake right current", intakeRightMotor.getOutputCurrent());
    }

    /**
     * Helper function used to control the state of both intake motors at the same
     * time. This allows the motors to be started and run in forwards or backwards
     * direction, or stopped. When started, motors are run at a constant speed.
     *
     * @param Start
     *            - if true, the intake motors are started. If false, they are
     *            stopped.
     * @param Inwards
     *            - if Start is true, this controls the motor direction, inwards
     *            (true) or outwards (false).
     */
    public void setIntakeMotorState(boolean Start, boolean Inwards)
    {
        double Speed;

        if (!Start)
        {
            Speed = 0.0;
        } else
        {
            Speed = INTAKE_SPEED;

            if (Inwards)
            {
                Speed *= -1;
            }
        }

        setIntakeSpeed(Speed);
    }

    /**
     * Toggles the swing (up/down) state of the intake mechanism.
     *
     * @return Returns true if the intake is swung up, false if down.
     */
    public boolean toggleIntakeSwing()
    {
        if (intakeIsUp)
        {
            setIntakeDown();
        } else
        {
            setIntakeDown();
        }

        return (intakeIsUp);
    }

    /**
     * Moves the intake mechanism into the up position.
     *
     * @return None
     */
    public void setIntakeUp()
    {
        intakeSolenoid.set(DoubleSolenoid.Value.kForward);
        intakeIsUp = true;
        SmartDashboard.putString("Intake", "up");

    }

    /**
     * Moves the intake mechanism into the down position.
     *
     * @return None
     */
    public void setIntakeDown()
    {
        intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
        intakeIsUp = false;
        SmartDashboard.putString("Intake", "down");
    }

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
