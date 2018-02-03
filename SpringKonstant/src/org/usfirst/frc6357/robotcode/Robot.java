
package org.usfirst.frc6357.robotcode;

import org.usfirst.frc6357.robotcode.commands.AutonomousCommand;
import org.usfirst.frc6357.robotcode.subsystems.DriveBaseSystem;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in 
 * the project.
 */
public class Robot extends TimedRobot 
{

    Command autonomousCommand;
    SendableChooser<Command> chooser = new SendableChooser<>();

    //Subsystems
    public static DriveBaseSystem driveBaseSystem;
    
    public static OI oi;
    
    public static Joystick operator, driver;

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
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Add commands to Autonomous Sendable Chooser

        chooser.addDefault("Autonomous Command", new AutonomousCommand());
        
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
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
        autonomousCommand = chooser.getSelected();
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
        Scheduler.getInstance().run();
        
        driveBaseSystem.setLeftSpeed(oi.getDriverJoystickValue(Ports.OIDriverLeftDrive));
        driveBaseSystem.setRightSpeed(oi.getDriverJoystickValue(Ports.OIDriverRightDrive));
        
        SmartDashboard.putData("Auto mode", chooser);
        SmartDashboard.putNumber("Joystick Y-Inputs", -1 * driver.getRawAxis(2));
        SmartDashboard.putNumber("Joystick Y-Inputs", -1 * driver.getRawAxis(5));
//       SmartDashboard.putNumber("Joystick X-Inputs", driver.getRawAxis(1));
//       SmartDashboard.putNumber("Joystick X-Inputs", driver.getRawAxis(4));
        SmartDashboard.putNumberArray("Joystick X-Inputs", new double[] {-1 * driver.getRawAxis(1), -1 * driver.getRawAxis(4)});
        SmartDashboard.putNumber("Joystick Left Trigger", driver.getRawAxis(3));
        SmartDashboard.putNumber("Joystick Right Trigger", driver.getRawAxis(3));
        //SmartDashboard.putNumber("Joystick X-Inputs", driver.getRawAxis(3));
        
        NetworkTable server = NetworkTableInstance.getDefault().getTable("SmartDashboard");
        server.getEntry("Joystick Y-Inputs").setDouble(0);
    }
}
