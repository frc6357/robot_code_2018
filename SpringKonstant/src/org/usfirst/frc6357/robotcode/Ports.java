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

    // All motors will be controlled by CAN so the IDs here
    // are CAN IDs. For switches and solenoids, the values are
    // RoboRio port numbers. For general purpose IO control, inputs
    // are assigned from port 0 upwards and outputs from port 9
    // downwards.

    public final int DriveLeftFrontMotor     = 10;
    public final int DriveLeftCenterMotor    = 11;
    public final int DriveLeftRearMotor      = 15;

    public final int DriveRightFrontMotor    = 12;
    public final int DriveRightCenterMotor   = 14;
    public final int DriveRightRearMotor     = 16;

    public final int DriveStrafeMotor        = 17;

    public final int DriveStrafeSolenoid     = 9;

    public final int DriveLeftEncoderA       = 2;
    public final int DriveLeftEncoderB       = 3;

    public final int DriveRightEncoderA      = 0;
    public final int DriveRightEncoderB      = 1;

    //*****************
    // Climb Subsystem
    //*****************
    public final int ClimbWinchMotor         = 20;
    public final int ClimbTopLimitSwitch     = 4;
    public final int ClimpBottomLimitSwitch  = 5;

    //******************
    // Intake Subsystem
    //******************
    public final int IntakeLeftMotor         = 30;
    public final int IntakeRightMotor        = 31;
    public final int IntakeTiltSolenoid      = 8;

    //***************
    // Arm Subsystem
    //***************
    public final int ArmElevationMotor       = 40;
    public final int ArmEncoderA             = 6;
    public final int ArmEncoderB             = 7;

    //
    // Driver's and operator's OI channel assignments
    //

    // TODO: Finalize these with the drivers. This assumes the same
    // Logitech game controllers as were used in 2017.
    //
    // See the IO subsystem specification for a graphic showing button
    // and axis IDs for the Logitech F310 gamepads in use.

    //********************
    // Drivers Controller
    //********************
    public final int OIDriverJoystick         = 0;

    public final int OIDriverLeftDrive        = 2; // Left Joystick Y
    public final int OIDriverRightDrive       = 5; // Right Joystick Y
    public final int OIDriverStrafe           = 3; // Lower triggers

    //*********************
    // Operator Controller
    //*********************
    public final int IOOperatorJoystick       = 1;

    public final int OIOperatorArm            = 1; // Left joystick Y

    public final int OIOperatorClimbDeploy    = 2; // Button B
    public final int OIOperatorClimbWinch     = 4; // Right joystick Y

    public final int OIOperatorIntakeIn       = 1; // Button A
    public final int OIOperatorIntakeOut      = 4; // Button Y
    public final int OIOperatorIntakeSwing    = 3; // Button X
}
