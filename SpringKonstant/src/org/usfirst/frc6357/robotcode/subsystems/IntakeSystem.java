package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Subsystem controlling the power cube intake mechanism in the robot. This mechanism comprises two motors connected to TalonSRX motor
 * controllers and a pneumatic actuator to swing the mechanism upwards into its stowed position. Current limits are set on the motors to
 * cause them to stop automatically when blocked.
 */
public class IntakeSystem extends Subsystem
{
    public IntakeSystem()
    {
    }

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
