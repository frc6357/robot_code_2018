// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc6357.SpringKonstant;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc6357.SpringKonstant.commands.*;
import org.usfirst.frc6357.SpringKonstant.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot 
{

    Command autonomousCommand;

    public static OI oi;
    
    // Subsystems
    public static GearDeploymentSystem gearDeploymentSystem;
    public static RopeClimbSystem ropeClimbSystem;
    public static DriveBaseSystem driveBaseSystem;
    
    // Joysticks
    public static Joystick driver;
    public static Joystick operator;
    
    // Actuators
    public static DoubleSolenoid doubleSolenoid1;
    public static SpeedController baseFrontLeft;
    public static SpeedController baseCenterLeft;
    public static SpeedController baseBackLeft;
    public static SpeedController baseFrontRight;
    public static SpeedController baseCenterRight;
    public static SpeedController baseBackRight;
    public static Compressor compressor1;
    //encoders
    public static Encoder encoderLeft;
	public static Encoder encoderRight;
	//gyroscope
	public static AnalogGyro gyro1;
	
	//Auto
	public static Auto auto;
 
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() 
    {	
    	// Actuators
    	compressor1 = new Compressor(0);
    	LiveWindow.addActuator("Compressor", "Compressor", compressor1);
    	
        doubleSolenoid1 = new DoubleSolenoid(0, 0, 1);										
        LiveWindow.addActuator("Gear Placement", "Double Solenoid 1", doubleSolenoid1);
       
        baseFrontLeft = new Talon(4);
        LiveWindow.addActuator("Base", "FrontLeft", (Talon) baseFrontLeft);
        
        baseCenterLeft = new Talon(2);
        LiveWindow.addActuator("Base", "Center Left", (Talon) baseCenterLeft);
        
        baseBackLeft = new Talon(0);
        LiveWindow.addActuator("Base", "Back Left", (Talon) baseBackLeft);
        
        baseFrontRight = new Talon(5);
        LiveWindow.addActuator("Base", "FrontRight", (Talon) baseFrontRight);
        
        baseCenterRight = new Talon(3);
        LiveWindow.addActuator("Base", "CetnerRight", (Talon) baseCenterRight);
        
        baseBackRight = new Talon(1);
        LiveWindow.addActuator("Base", "Back Right", (Talon) baseBackRight);
    	
    	// Subsystems
    	gearDeploymentSystem = new GearDeploymentSystem();
    	ropeClimbSystem = new RopeClimbSystem();
    	driveBaseSystem = new DriveBaseSystem(baseFrontLeft,  baseCenterLeft, baseBackLeft, baseFrontRight, baseCenterRight, baseBackRight);
        
    	//Auto
        auto = new Auto(encoderRight, encoderLeft, driveBaseSystem);

    	
        // OI must be constructed after subsystems. If the OI creates Commands
        //(which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // instantiate the command used for the autonomous period
        
        //Encoders 
        encoderLeft = new Encoder(2, 3);
        encoderRight = new Encoder(0, 1);
        
        // IMPORTANT CONVENTION: ALWYAS FEET PER SECOND
        final double DistancePerPulse = (3.1415926539*4.0/12.0)/384.0;
        
        encoderLeft.setDistancePerPulse(DistancePerPulse);
        encoderRight.setDistancePerPulse(DistancePerPulse);
        
        
    
        //GyroScope 
        gyro1 = new AnalogGyro(1);
        
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit()
    {

    }

    public void disabledPeriodic() 
    {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("rvel", encoderRight.getRate());
        SmartDashboard.putNumber("lvel", encoderLeft.getRate());
        SmartDashboard.putNumber("rpos", encoderRight.getDistance());
        SmartDashboard.putNumber("lpos", encoderLeft.getDistance());
    }

    public void autonomousInit() 
    {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
        gyro1.calibrate();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
        Scheduler.getInstance().run();
    }

    public void teleopInit() 
    {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        
        driver = oi.getDriver();
        operator = oi.getOperator();
        compressor1.start();
        compressor1.enabled();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
        Scheduler.getInstance().run();
        driveBaseSystem.setLeftMotors(-1 * driver.getRawAxis(1));
        driveBaseSystem.setRightMotors(driver.getRawAxis(5));
        
        SmartDashboard.putNumber("rvel", encoderRight.getRate());
        SmartDashboard.putNumber("lvel", encoderLeft.getRate());
        SmartDashboard.putNumber("rpos", encoderRight.getDistance());
        SmartDashboard.putNumber("lpos", encoderLeft.getDistance());
    
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() 
    {
        LiveWindow.run();
    }
}
