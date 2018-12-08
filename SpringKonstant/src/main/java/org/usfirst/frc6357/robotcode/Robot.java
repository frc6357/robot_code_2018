package org.usfirst.frc6357.robotcode;

//import java.io.IOException;

import org.usfirst.frc6357.robotcode.commands.AutonomousCommand;
import org.usfirst.frc6357.robotcode.subsystems.DriveBaseSystem;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to each mode, as described in the
 * TimedRobot documentation. If you change the name of this class or the package after creating this project, you must also update the
 * build.properties file in the project.
 */
public class Robot extends TimedRobot
{
    Command autonomousCommand;
    public static SendableChooser<String> chooserStart = new SendableChooser<>(); // Allows the user to pick the starting point
    public static SendableChooser<String> chooserOverride = new SendableChooser<>();
    
    // Subsystems
    public static DriveBaseSystem driveBaseSystem;
    public static OI oi;

    int counter = 0;

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
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
    }

    /**
     * This function is called when the disabled button is hit. You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit()
    {
    }

    /**
     * This function is called continually while the robot is disabled.
     */
    @Override
    public void disabledPeriodic()
    {
        Scheduler.getInstance().run();
    }
    
    /**
     * This function is called when the autonomous period begins
     */
    @Override
    public void autonomousInit()
    {
        autonomousCommand = new AutonomousCommand(chooserStart.getSelected(), chooserOverride.getSelected());

        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic()
    {
//        SmartDashboard.putNumber("LIDAR Distance", distanceLIDAR.getDistance());
        Scheduler.getInstance().run();
    }

    /**
     * This function is called at the beginning of teleop
     */
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
        double driveLeft, driveRight;

        Scheduler.getInstance().run();

        driveLeft = oi.getDriverJoystickValue(Ports.OIDriverLeftDrive, true); // Retrieves the status of all buttons and joysticks
        driveRight = oi.getDriverJoystickValue(Ports.OIDriverRightDrive, true);

        driveBaseSystem.setLeftSpeed(driveLeft); // Listens to input and drives the robot
        driveBaseSystem.setRightSpeed(driveRight);

        if(counter % 10 == 0)
        {
            SmartDashboard.putNumber("Left Speed (actual)", driveBaseSystem.getLeftSpeed()); // Put data onto Shuffleboard for reading
            SmartDashboard.putNumber("Right Speed (actual)", driveBaseSystem.getRightSpeed());
            SmartDashboard.putNumber("Left Speed (set)", driveLeft); // Put data onto Shuffleboard for reading
            SmartDashboard.putNumber("Right Speed (set)", driveRight);
        }
        counter++;
    }
}