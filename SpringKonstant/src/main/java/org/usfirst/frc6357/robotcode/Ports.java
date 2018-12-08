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
    public static final int pcm1                        = 1; // CAN ID 1
    public static final int pcm2                        = 2; // CAN ID 2

    // *****************
    // Drive Subsystem
    // *****************
    public static final int driveLeftFrontMotor         = 11; // CAN ID 11
    public static final int driveLeftCenterMotor        = 15; // CAN ID 15
    public static final int driveLeftRearMotor          = 10; // CAN ID 10

    public static final int driveRightFrontMotor        = 16; // CAN ID 16
    public static final int driveRightCenterMotor       = 14; // CAN ID 14
    public static final int driveRightRearMotor         = 12; // CAN ID 12

    // Pneumatics for drive lift mechanisms.
    //
    // This assumes that we use a double solenoid for the H-wheel lifter
    // but only single solenoids for the front and back lifters where gravity
    // should be sufficient to drop the drive when lift is disengaged.
    //
    // We also use a double solenoid to control both side's low/high gear
    // selection.
    public static final int drivePCM                    = pcm1;
    public static final int frontButterflyDown          = 0; // PCM 1 output 0, pushes down front butterfly
    public static final int backButterflyDown           = 1; // PCM 1 output 1, pushes down back butterfly
    public static final int hDriveSolenoid              = 4; // PCM 1 output 4, engages H-Drive
    public static final int driveGearShiftHigh          = 3; // PCM 1 output 3, front port of drive shift
    public static final int driveGearShiftLow           = 5; // PCM 1 output 5, back port of drive shift
    public static final int ESolenoidFront              = 2; // PCM 1 output 2 (fwd channel), part of fifth unused channel
    public static final int ESolenoidBack               = 6; // PCM 1 output 6 (reverse channel), part of unused fifth channel

    //
    // Driver's and operator's OI channel assignments
    //

    // See the IO subsystem specification for a graphic showing button
    // and axis IDs for the Logitech F310 gamepads in use.

    // ********************
    // Drivers Controller
    // ********************
    public static final int OIDriverJoystick            = 0;

    public static final int OIDriverLeftDrive           = 1; // Left Joystick Y
    public static final int OIDriverRightDrive          = 5; // Right Joystick Y
    public static final int OIDriverSlow                = 5;         // Left bumper
    public static final int IODriverGearSelectLow       = 1; // Button A
    public static final int IODriverGearSelectHigh      = 4; // Button Y

    // *********************
    // Operator Controller
    // *********************
    public static final int OIOperatorJoystick          = 1;
}
