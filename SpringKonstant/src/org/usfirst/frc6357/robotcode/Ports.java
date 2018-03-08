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
    //
    // RoboRio Channel Assignments for robot motors and actuators
    //

    // **********************
    // Assorted peripherals
    // **********************
    public static final int pcm1 = 1; // PCM 1, CAN ID 1
    public static final int pcm2 = 2; // PCM 2, CAN ID 2

    // *****************
    // Drive Subsystem
    // *****************
    public static final int driveLeftFrontMotor = 11; // CAN ID 11
    public static final int driveLeftCenterMotor = 15; // CAN ID 15
    public static final int driveLeftRearMotor = 10; // CAN ID 10

    public static final int driveRightFrontMotor = 16; // CAN ID 16
    public static final int driveRightCenterMotor = 14; // CAN ID 14
    public static final int driveRightRearMotor = 12; // CAN ID 12

    public static final int driveStrafeMotor = 17; // CAN ID 17

    // Pneumatics for drive lift mechanisms.
    //
    // This assumes that we use a double solenoid for the H-wheel lifter
    // but only single solenoids for the front and back lifters where gravity
    // should be sufficient to drop the drive when lift is disengaged.
    //
    // We also use a double solenoid to control both side's low/high gear
    // selection.
    public static final int frontButterflyDown = 0; // PCM 1 output 0, pushes down front butterfly
    public static final int backButterflyDown = 1; // PCM 1 output 1, pushes down back butterfly
    public static final int hDriveSolenoid = 4; // PCM 1 output 4, engages H-Drive
    public static final int driveGearShiftHigh = 3; // PCM 1 output 3, front port of drive shift
    public static final int driveGearShiftLow = 5; // PCM 1 output 5, back port of drive shift
    public static final int ESolenoidFront = 2; // PDM 1 output 2 (fwd channel), part of fifth unused channel
    public static final int ESolenoidBack = 6; // PDM 1 output 6 (reverse channel), part of unused fifth channel

    public static final int DriveLeftEncoderA = 2; // DIO input 2
    public static final int DriveLeftEncoderB = 3; // DIO input 1

    public static final int DriveRightEncoderA = 0; // DIO input 0
    public static final int DriveRightEncoderB = 1; // DIO input 1

    // *****************
    // Climb Subsystem
    // *****************
    public static final int ClimbWinchMotor = 20; // CAN ID 20

    public static final int ClimbTopLimitSwitch = 4; // DIO input 4 - normally closed
    public static final int ClimpBottomLimitSwitch = 5; // DIO input 5 - normally closed

    // ******************
    // Intake Subsystem
    // ******************
    public static final int IntakeLeftMotor = 30; // CAN ID 30
    public static final int IntakeRightMotor = 31; // CAN ID 31

    public static final int IntakeTiltSolenoidUp = 6; // PCM 1 output 6 (forward channel)
    public static final int IntakeTiltSolenoidDown = 7; // PCM 1 output 7 (reverse channel)

    // TODO: Check the polarity of these two - may need to reverse.
    public static final int IntakeGripSolenoidIn = 6; // PCM 2 output 0 (forward channel)
    public static final int IntakeGripSolenoidOut = 7; // PCM 2 output 1 (reverse channel)

    // ***************
    // Arm Subsystem
    // ***************
    public static final int ArmElevationMotor = 40; // CAN ID 40

    public static final int ArmEncoderA = 6; // DIO input 6
    public static final int ArmEncoderB = 7; // DIO input 7
    public static final int ArmLimitTop = 8; // DIO input 8
    public static final int ArmLimitBottom = 9; // DIO input 9
    
    public static final int floorTime = 3500; //Time for robot to lower to the floor, in milliseconds
    public static final int switchTime = 1000;    //Time from floor to switch
    public static final int midScaleTime = 2500;  //Time from floor to middle of scale
    public static final int highScaleTime = 3500; //Time from floor to top of scale

    //
    // Driver's and operator's OI channel assignments
    //

    // See the IO subsystem specification for a graphic showing button
    // and axis IDs for the Logitech F310 gamepads in use.

    // ********************
    // Drivers Controller
    // ********************
    public static final int OIDriverJoystick = 0;

    public static final int OIDriverLeftDrive = 1; // Left Joystick Y
    public static final int OIDriverRightDrive = 5; // Right Joystick Y
    public static final int OIDriverStrafeRight = 3; // Right Trigger
    public static final int OIDriverStrafeLeft = 2; // Left Trigger

    // This is for testing only. Button B on the driver controller will
    // toggle the strafing wheel. In the final code, it is hoped that this
    // will be done automatically depending upon the state of the drive
    // joysticks.
    public static final int OIDriverStrafeDeploy = 5; // Left bumper
    public static final int OIDriverStrafeStow = 6; // Right bumper

    public static final int IODriverGearSelectLow = 1; // Button A
    public static final int IODriverGearSelectHigh = 4; // Button Y

    // *********************
    // Operator Controller
    // *********************
    public static final int OIOperatorJoystick = 1;

    public static final int OIOperatorArm = 1; // Left joystick Y

    public static final int OIOperatorClimbWinch = 4; // Right joystick Y

    public static final int OIOperatorIntakeIn = 1; // Button A
    public static final int OIOperatorIntakeOut = 4; // Button Y
    public static final int OIOperatorIntakeSwing = 3; // Button X
}