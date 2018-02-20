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
    public final DoubleSolenoid intakeLiftSolenoid;
    public final DoubleSolenoid intakeGripSolenoid;
    public boolean intakeIsUp = true;

    public IntakeSystem()
    {
        // Pneumatic control for the intake grippers and lift mechanism.
        intakeLiftSolenoid = new DoubleSolenoid(Ports.pcm1, Ports.IntakeTiltSolenoidUp, Ports.IntakeTiltSolenoidDown);
        intakeGripSolenoid = new DoubleSolenoid(Ports.pcm2, Ports.IntakeGripSolenoidIn, Ports.IntakeGripSolenoidOut);

<<<<<<< HEAD
        intakeSolenoid = new DoubleSolenoid(Ports.PCM_ID, Ports.IntakeTiltSolenoidUp, Ports.IntakeTiltSolenoidDown);
        
        intakeLeftMotor = new WPI_TalonSRX(Ports.ArmElevationMotor);
        intakeRightMotor = new WPI_TalonSRX(Ports.ArmElevationMotor);
=======
        intakeLeftMotor    = new WPI_TalonSRX(Ports.ArmElevationMotor);
        intakeRightMotor   = new WPI_TalonSRX(Ports.ArmElevationMotor);
>>>>>>> 3d446e391a754a95700b4b58c7377ed22339ba17

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
        SmartDashboard.putNumber("Intake left current", intakeLeftMotor.getOutputCurrent());
        SmartDashboard.putNumber("Intake right current", intakeRightMotor.getOutputCurrent());
    }

    /**
     * Helper function used to control the state of both intake motors at the same
     * time. This allows the motors to be started and run in forwards or backwards
     * direction, or stopped. When started, motors are run at a constant speed.
     *
     * @param start
     *            - if true, the intake motors are started. If false, they are
     *            stopped.
     * @param inwards
     *            - if Start is true, this controls the motor direction, inwards
     *            (true) or outwards (false).
     */
    public void setIntakeMotorState(boolean start, boolean inwards)
    {
        double speed;

        if (!start)
        {
            speed = 0.0;
        }
        else
        {
            speed = INTAKE_SPEED;

            if (inwards)
            {
                speed *= -1;
            }
        }

        setIntakeSpeed(speed);
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
        }
        else
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
        intakeLiftSolenoid.set(DoubleSolenoid.Value.kForward);
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
        intakeLiftSolenoid.set(DoubleSolenoid.Value.kReverse);
        intakeIsUp = false;
        SmartDashboard.putString("Intake", "down");
    }

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
