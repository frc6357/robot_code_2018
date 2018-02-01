// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.springkonstant.robot.subsystems;

//import org.springkonstant.robot.commands.*;

import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.Solenoid;
//import edu.wpi.first.wpilibj.SpeedController;
//import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class GearDeploymentSystem extends Subsystem
{
	private final DoubleSolenoid gearDoubleSolenoidRight;
	private final DoubleSolenoid gearDoubleSolenoidLeft;
	private final DoubleSolenoid gearDoubleSolenoidPush;

	public GearDeploymentSystem(DoubleSolenoid gearDoubleSolenoidLeftin, DoubleSolenoid gearDoubleSolenoidRightin,
			DoubleSolenoid gearDoubleSolenoidPushin)
	{
		gearDoubleSolenoidLeft = gearDoubleSolenoidLeftin;
		gearDoubleSolenoidRight = gearDoubleSolenoidRightin;
		gearDoubleSolenoidPush = gearDoubleSolenoidPushin;

	}

	public void pushGear()
	{
		gearDoubleSolenoidPush.set(DoubleSolenoid.Value.kForward);
	}

	public void resetPush()
	{
		gearDoubleSolenoidPush.set(DoubleSolenoid.Value.kReverse);
	}

	public void gearSlideDown()
	{
		gearDoubleSolenoidLeft.set(DoubleSolenoid.Value.kReverse);
		gearDoubleSolenoidRight.set(DoubleSolenoid.Value.kReverse);
	}

	public void gearSlideUp()
	{
		gearDoubleSolenoidLeft.set(DoubleSolenoid.Value.kForward);
		gearDoubleSolenoidRight.set(DoubleSolenoid.Value.kForward);
	}

	public void resetSolenoids()
	{
		resetPush();
		gearSlideDown();
	}

	public void initDefaultCommand()
	{
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

}
