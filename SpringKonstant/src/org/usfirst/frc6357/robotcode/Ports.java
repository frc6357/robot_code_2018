/**
 *
 */
package org.usfirst.frc6357.robotcode;

/**
 *
 * This class defines the connections of all actuators and controllers to the RoboRio
 * and allocation of driver and operator controller user interface joysticks and buttons.
 * It is intended to be used by all classes which would otherwise hardcode these values
 * on the understanding that using a decscriptive name is very much clearer than an
 * opaque number.
 *
 * @author dave
 *
 */
public class Ports
{
    // TODO: The numbers here are completely fictitious for now. Get with the
    // hardware teams to determine the actual port assignments! Anything assigned
    // value -1 has not been checked with the designers and is currently invalid.
    // Drive motors are set to match the 2017 robot assignments.

    //
    // RoboRio Channel Assignments for robot motors and actuators
    //

    //*****************
    // Drive Subsystem
    //*****************
    public final int DriveLeftFrontMotor     = 10;
    public final int DriveLeftCenterMotor    = 11;
    public final int DriveLeftRearMotor      = 15;

    public final int DriveRightFrontMotor    = 12;
    public final int DriveRightCenterMotor   = 14;
    public final int DriveRightRearMotor     = 16;

    public final int DriveStrafeMotor        = -1;

    public final int DriveStrafeSolenoid     = -1;

    public final int DriveLeftEncoderA       = 2;
    public final int DriveLeftEncoderB       = 3;

    public final int DriveRightEncoderA      = 0;
    public final int DriveRightEncoderB      = 1;

    //*****************
    // Climb Subsystem
    //*****************
    public final int ClimbWinchMotor         = 20;

    //******************
    // Intake Subsystem
    //******************
    public final int IntakeLeftMotor         = -1;
    public final int IntakeRightMotor        = -1;
    public final int IntakeLimitSwitch       = -1;

    //***************
    // Arm Subsystem
    //***************
    public final int ArmElevationMotor       = -1;
    public final int ArmTopLimitSwitch       = -1;
    public final int ArmBottomLimitSwitch    = -1;
    public final int ArmEncoderA             = -1;
    public final int ArmEncoderB             = -1;

    //
    // Driver's and operator's OI channel assignments
    //

    // TODO: Finalize these with the drivers. This assumes the same
    // Logitech game controllers as were used in 2017

    //********************
    // Drivers Controller
    //********************
    public final int OIDriverJoystick         = 0;

    public final int OIDriverLeftDrive        = -1;
    public final int OIDriverRightDrive       = -1;
    public final int OIDriverLeftStrafe       = -1;
    public final int OIDriverRightStrafe      = -1;

    //*********************
    // Operator Controller
    //*********************
    public final int IOOperatorJoystick       = 1;

    public final int OIOperatorArmUp          = -1;
    public final int OIOperatorArmDown        = -1;

    public final int OIOperatorClimbUp        = -1;
    public final int OIOperatorClimbDown      = -1;

    public final int OIOperatorIntakeIn       = -1;
    public final int OIOperatorIntakeOut      = -1;
    public final int OIOperatorIntakeSwing    = -1;
}
