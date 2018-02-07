
package org.usfirst.frc6357.robotcode;

import java.io.IOException;

import org.usfirst.frc6357.robotcode.commands.AutonomousCommand;
import org.usfirst.frc6357.robotcode.subsystems.DriveBaseSystem;
import org.usfirst.frc6357.robotcode.tools.CSVReader;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot
{

	Command autonomousCommand;
	SendableChooser<String> chooser = new SendableChooser<>(); //Lets you pick from a group of commands, whose references are strings

	// Subsystems
	public static DriveBaseSystem driveBaseSystem;

	public static OI oi;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit()
	{
		// Subsystem Creation
		driveBaseSystem = new DriveBaseSystem();

		// OI must be constructed after subsystems. If the OI creates Commands
		// (which it very likely will), subsystems are not guaranteed to be
		// constructed yet. Thus, their requires() statements may grab null
		// pointers. Bad news. Don't move it.
		oi = new OI();

		// Add commands to Autonomous Sendable Chooser

		chooser.addDefault("CSV 1", "src/org/usfirst/frc6457/robotcode/commands/AutoSheets/Test.csv");
	}

	/**
	 * This function is called when the disabled button is hit. You can use it
	 * to reset subsystems before shutting down.
	 */
	@Override
	public void disabledInit()
	{

	}

	@Override
	public void disabledPeriodic()
	{
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit()
	{
		try
		{
			autonomousCommand = new AutonomousCommand(CSVReader.parse(chooser.getSelected()));
		}
		catch (IOException e)
		{
		}

		// schedule the autonomous command (example)
		if (autonomousCommand != null) autonomousCommand.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic()
	{
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit()
	{
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) autonomousCommand.cancel();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic()
	{
		double driveLeft, driveRight, driveStrafe;

		Scheduler.getInstance().run();

		driveLeft = oi.getDriverJoystickValue(Ports.OIDriverLeftDrive);
		driveRight = oi.getDriverJoystickValue(Ports.OIDriverRightDrive);
		driveStrafe = oi.getDriverJoystickValue(Ports.OIDriverStrafe);

		driveBaseSystem.setLeftSpeed(driveLeft);
		driveBaseSystem.setRightSpeed(driveRight);
		driveBaseSystem.setStrafeSpeed(driveStrafe);

		SmartDashboard.putData("Auto mode", chooser);
		SmartDashboard.putNumber("Drive Left Raw", driveLeft);
		SmartDashboard.putNumber("Drive Right Raw", driveRight);
		SmartDashboard.putNumber("Drive Strafe Raw", driveStrafe);
		SmartDashboard.putBoolean("Strafe Deployed", driveBaseSystem.getStrafeState());

		NetworkTable server = NetworkTableInstance.getDefault().getTable("SmartDashboard");
		server.getEntry("Joystick Y-Inputs").setDouble(0);
	}
}
