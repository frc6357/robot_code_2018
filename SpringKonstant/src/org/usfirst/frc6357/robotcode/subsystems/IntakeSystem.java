package org.usfirst.frc6357.robotcode.subsystems;

import org.usfirst.frc6357.robotcode.Ports;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Subsystem controlling the power cube intake mechanism in the robot. This mechanism comprises two motors connected to TalonSRX motor
 * controllers and a pneumatic actuator to swing the mechanism upwards into its stowed position. Current limits are set on the motors to
 * cause them to stop automatically when blocked.
 */
public class IntakeSystem extends Subsystem
{

    private final Solenoid grippers;

    private static final double INTAKE_SPEED = 0.25;
    public final WPI_TalonSRX intakeLeftMotor;
    public final WPI_TalonSRX intakeRightMotor;
    // public final DoubleSolenoid intakeLiftSolenoid;
    // public final DoubleSolenoid intakeGripSolenoid;
    public boolean intakeIsUp = true;
    public boolean gripperOpen = true;

    public IntakeSystem()
    {
        // Pneumatic control for the intake grippers and lift mechanism.

        // intakeGripSolenoid = new DoubleSolenoid(Ports.pcm, Ports.IntakeGripSolenoidIn, Ports.IntakeGripSolenoidOut);
        // intakeLiftSolenoid = new DoubleSolenoid(Ports.pcm1, Ports.IntakeTiltSolenoidUp, Ports.IntakeTiltSolenoidDown);

        intakeLeftMotor = new WPI_TalonSRX(Ports.IntakeLeftMotor);
        intakeRightMotor = new WPI_TalonSRX(Ports.IntakeRightMotor);

        grippers = new Solenoid(Ports.pcm1, Ports.hDriveSolenoid);

        // TODO: Set current limits sensibly so that we catch case where a cube is
        // pulled into the mechanism and the motor stalls.

        /*
         * intakeRightMotor.configPeakCurrentLimit(35, 10); // 35 A intakeRightMotor.configPeakCurrentDuration(200, 10); // 200ms
         * intakeRightMotor.configContinuousCurrentLimit(30, 10); // 30A intakeRightMotor.enableCurrentLimit(true);
         *
         * intakeRightMotor.setInverted(true); intakeRightMotor.set(ControlMode.Follower, intakeLeftMotor.getDeviceID());
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
     *            The speed setting for the intake motors. Valid values are in the range -1.0 to 1.0.
     *
     * @return None
     */
    public void setIntakeSpeed(double leftAxis, double rightAxis)
    {
        double speed = leftAxis - rightAxis;

        intakeLeftMotor.set(speed);
        intakeRightMotor.set(speed);

        SmartDashboard.putNumber("Intake speed", speed);
        SmartDashboard.putNumber("Intake left current", intakeLeftMotor.getOutputCurrent());
        SmartDashboard.putNumber("Intake right current", intakeRightMotor.getOutputCurrent());
    }

    /**
     * Opens up the gripper
     */
    public void openGripper()
    {
        grippers.set(true);
    }

    /**
     * Closes the gripper to hold box
     */
    public void closeGripper()
    {
        grippers.set(false);
    }

    /**
     * Toggles the swing (up/down) state of the intake mechanism.
     *
     * @return Returns true if the intake is swung up, false if down.
     */
    /*
     * public boolean toggleIntakeSwing() { if (intakeIsUp) { setIntakeDown(); } else { setIntakeDown(); }
     * 
     * return (intakeIsUp); }
     */

    /**
     * Moves the intake mechanism into the up position.
     *
     * @return None
     */
    /*
     * public void setIntakeUp() { intakeLiftSolenoid.set(DoubleSolenoid.Value.kForward); intakeIsUp = true;
     * SmartDashboard.putString("Intake", "up");
     * 
     * }
     */
    /**
     * Moves the intake mechanism into the down position.
     *
     * @return None
     */
    /*
     * public void setIntakeDown() { intakeLiftSolenoid.set(DoubleSolenoid.Value.kReverse); intakeIsUp = false;
     * SmartDashboard.putString("Intake", "down"); }
     */
    /**
     * Sets the gripper to close or open Open == True Close == False
     * 
     * @param state
     *            - boolean state of the grippers
     *//*
       * public void setIntakeGrippers(boolean state) { if(state) { openIntakeGripper(); } else { closeIntakeGripper(); } }
       */

    /**
     * Opens the gripper
     */
    /*
     * public void openIntakeGripper() { intakeGripSolenoid.set(DoubleSolenoid.Value.kReverse); gripperOpen = true; }
     * 
     * /** Closes the gripper
     */
    /*
     * public void closeIntakeGripper() { intakeGripSolenoid.set(DoubleSolenoid.Value.kForward); gripperOpen = false; }
     */

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
