/**
 *
 */
package org.usfirst.frc6357.robotcode;

/**
 *
 * This class defines the connections of all actuators and controllers to the
 * RoboRio and allocation of driver and operator controller user interface
 * joysticks and buttons. It is intended to be used by all classes which would
 * otherwise hardcode these values on the understanding that using a
 * decscriptive name is very much clearer than an opaque number.
 *
 * @author DW
 *
 */
public class Ports
{
    // TODO: The numbers here are completely fictitious for now. Get with the
    // hardware teams to determine the actual port assignments! Drive motors are set
    // to match the 2017 robot assignments.

    //
    // RoboRio Channel Assignments for robot motors and actuators
    //

    // **********************
    //  Assorted peripherals
    // **********************
    public static final int PCM_ID                  = 1; // CAN ID 1

    // *****************
    // Drive Subsystem
    // *****************
    public static final int DriveLeftFrontMotor     = 10; // CAN ID 10
    public static final int DriveLeftCenterMotor    = 11; // CAN ID 11
    public static final int DriveLeftRearMotor      = 15; // CAN ID 15

    public static final int DriveRightFrontMotor    = 12; // CAN ID 12
    public static final int DriveRightCenterMotor   = 14; // CAN ID 14
    public static final int DriveRightRearMotor     = 16; // CAN ID 16

    public static final int DriveStrafeMotor        = 17; // CAN ID 17

    public static final int DriveStrafeSolenoid     = 0;  // PCM output 0

    public static final int DriveLeftEncoderA       = 2;  // DIO input 2
    public static final int DriveLeftEncoderB       = 3;  // DIO input 1

    public static final int DriveRightEncoderA      = 0;  // DIO input 0
    public static final int DriveRightEncoderB      = 1;  // DIO input 1

    // *****************
    // Climb Subsystem
    // *****************
    public static final int ClimbWinchMotor         = 20;  // CAN ID 20
    public static final int ClimbTopLimitSwitch     = 4;   // DIO input 4 - normally open
    public static final int ClimpBottomLimitSwitch  = 5;   // DIO input 5 - normally open

    // ******************
    // Intake Subsystem
    // ******************
    public static final int IntakeLeftMotor         = 30;  // CAN ID 30
    public static final int IntakeRightMotor        = 31;  // CAN ID 31
    public static final int IntakeTiltSolenoid      = 1;   // PCM output 1

    // ***************
    // Arm Subsystem
    // ***************
    public static final int ArmElevationMotor       = 40;  // CAN ID 40
    public static final int ArmEncoderA             = 6;   // DIO input 6
    public static final int ArmEncoderB             = 7;   // DIO input 7

    //
    // Driver's and operator's OI channel assignments
    //

    // TODO: Finalize these with the drivers. This assumes the same
    // Logitech game controllers as were used in 2017.
    //
    // See the IO subsystem specification for a graphic showing button
    // and axis IDs for the Logitech F310 gamepads in use.

    // ********************
    // Drivers Controller
    // ********************
    public static final int OIDriverJoystick        = 0;

    public static final int OIDriverLeftDrive       = 1;    // Left Joystick Y
    public static final int OIDriverRightDrive      = 5;    // Right Joystick Y
    public static final int OIDriverStrafe          = 3;    // Lower triggers

    // This is for testing only. Button B on the driver controller will
    // toggle the strafing wheel. In the final code, it is hoped that this
    // will be done automatically depending upon the state of the drive
    // joysticks.
    public static final int OIDriverStrafeDeploy    = 5;   // Left bumper
    public static final int OIDriverStrafeStow      = 6;   // Right bumper

    // *********************
    // Operator Controller
    // *********************
    public static final int OIOperatorJoystick      = 1;

    public static final int OIOperatorArm           = 1;    // Left joystick Y

    public static final int OIOperatorClimbWinch    = 4;    // Right joystick Y

    public static final int OIOperatorIntakeIn      = 1;    // Button A
    public static final int OIOperatorIntakeOut     = 4;    // Button Y
    public static final int OIOperatorIntakeSwing   = 3;    // Button X
}
