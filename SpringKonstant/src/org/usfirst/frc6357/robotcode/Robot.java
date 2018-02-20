package org.usfirst.frc6357.robotcode;

import java.io.IOException;

import org.usfirst.frc6357.robotcode.commands.AutonomousCommand;
import org.usfirst.frc6357.robotcode.commands.GearShiftCommand;
import org.usfirst.frc6357.robotcode.commands.IntakeCommand;
import org.usfirst.frc6357.robotcode.commands.IntakeSwingToggle;
import org.usfirst.frc6357.robotcode.commands.StrafeDeploy;
import org.usfirst.frc6357.robotcode.commands.StrafeStow;
import org.usfirst.frc6357.robotcode.commands.TestPidPosition;
import org.usfirst.frc6357.robotcode.subsystems.ArmSystem;
import org.usfirst.frc6357.robotcode.subsystems.ClimbSystem;
import org.usfirst.frc6357.robotcode.subsystems.DriveBaseSystem;
import org.usfirst.frc6357.robotcode.subsystems.IntakeSystem;
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
    SendableChooser<String> chooser = new SendableChooser<>(); // Lets you pick from a group of commands, whose
                                                               // references are strings

    // Subsystems
    public static DriveBaseSystem driveBaseSystem;
    public static ClimbSystem climbSystem;
    public static ArmSystem armSystem;
    public static IntakeSystem intakeSystem;

    public static OI oi;

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit()
    {
        // Subsystem Creation
        driveBaseSystem = new DriveBaseSystem();
        climbSystem = new ClimbSystem();
        armSystem = new ArmSystem();
        intakeSystem = new IntakeSystem();

        // OI must be constructed after subsystems. If the OI creates Commands
        // (which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Add commands to Autonomous Sendable Chooser

        chooser.addDefault("Test", "/home/lvuser/AutoSheets/Test.csv");
        chooser.addObject("Left Start to Left Switch", "/home/lvuser/AutoSheets/CSV 1.csv");
        chooser.addObject("Left Start to Right Switch", "/home/lvuser/AutoSheets/CSV 2.csv");
        chooser.addObject("Left Start to Left Scale", "/home/lvuser/AutoSheets/CSV 3.csv");
        chooser.addObject("Left Start to Right Scale", "/home/lvuser/AutoSheets/CSV 4.csv");
        chooser.addObject("Right Start to Left Switch", "/home/lvuser/AutoSheets/CSV 5.csv");
        chooser.addObject("Right Start to Right Switch", "/home/lvuser/AutoSheets/CSV 6.csv");
        chooser.addObject("Right Start to Left Scale", "/home/lvuser/AutoSheets/CSV 7.csv");
        chooser.addObject("Right Start to Right Scale", "/home/lvuser/AutoSheets/CSV 8.csv");
        chooser.addObject("Left Start to Right Switch Alt", "/home/lvuser/AutoSheets/CSV 9.csv");
        chooser.addObject("Left Start to Right Scale Alt", "/home/lvuser/AutoSheets/CSV 10.csv");
        chooser.addObject("Right Start to Left Switch Alt", "/home/lvuser/AutoSheets/CSV 11.csv");
        chooser.addObject("Right Start to Left Scale Alt", "/home/lvuser/AutoSheets/CSV 12.csv");
        chooser.addObject("Any Start, drive straight", "/home/lvuser/AutoSheets/CSV 13.csv");
    }

    /**
     * This function is called when the disabled button is hit. You can use it to
     * reset subsystems before shutting down.
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
        } catch (IOException e)
        {
            System.out.println("Exception here: " + e);
        }

       // autonomousCommand = new TestPidPosition();
        // schedule the autonomous command (example)
        if (autonomousCommand != null)
            autonomousCommand.start();
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
        if (autonomousCommand != null)
            autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic()
    {
        double driveLeft, driveRight, driveStrafeLeft, driveStrafeRight, robotAngle, climbSpeed, armSpeed;
        double rotateAdjust, lAdjust, rAdjust;
        
        Scheduler.getInstance().run();
        
        driveLeft = oi.getDriverJoystickValue(Ports.OIDriverLeftDrive, true);
        driveRight = oi.getDriverJoystickValue(Ports.OIDriverRightDrive, true);
        driveStrafeRight = oi.getDriverJoystickValue(Ports.OIDriverStrafeRight, true);
        driveStrafeLeft = oi.getDriverJoystickValue(Ports.OIDriverStrafeLeft, true);
        climbSpeed = oi.getOperatorJoystickValue(Ports.OIOperatorClimbWinch, false);
        armSpeed = oi.getOperatorJoystickValue(Ports.OIOperatorArm, false);
        

        robotAngle = driveBaseSystem.driveIMU.updatePeriodic();

        rotateAdjust = driveBaseSystem.getStrafeRotateAdjust();
        lAdjust = rotateAdjust / 2;
        rAdjust = -lAdjust;
        
        driveBaseSystem.setLeftSpeed(driveLeft);
        driveBaseSystem.setRightSpeed(driveRight);
        driveBaseSystem.setStrafeSpeed(driveStrafeRight, driveStrafeLeft);

        climbSystem.setWinchSpeed(climbSpeed);
        //armSystem.periodic(armSpeed);
        armSystem.setMotorSpeed(armSpeed);
        
        SmartDashboard.putData("Deploy strafe", new StrafeDeploy());
        SmartDashboard.putData("Stow strafe", new StrafeStow());
        SmartDashboard.putData("Shift Gears up", new GearShiftCommand(true));
        SmartDashboard.putData("Shift Gears down", new GearShiftCommand(false));
        SmartDashboard.putData("Turn intake on and inwards", new IntakeCommand(true, true));
        SmartDashboard.putData("Turn intake on and outwards", new IntakeCommand(true, false));
        SmartDashboard.putData("Turn intake off", new IntakeCommand(false, false));
        SmartDashboard.putData("Toggle intake swing", new IntakeSwingToggle());
        
        SmartDashboard.putNumber("Left Encoder Raw", driveBaseSystem.getLeftEncoderRaw());
        SmartDashboard.putNumber("Right Encoder Raw", driveBaseSystem.getRightEncoderRaw());
        SmartDashboard.putNumber("Left Encoder Rate", driveBaseSystem.getLeftEncoderRate());
        SmartDashboard.putNumber("Right Encoder Rate", driveBaseSystem.getRightEncoderRate());
        SmartDashboard.putData("Auto mode", chooser);
        SmartDashboard.putNumber("Drive Left Raw", driveLeft);
        SmartDashboard.putNumber("Drive Right Raw", driveRight);
        SmartDashboard.putNumber("Drive Strafe Right", driveStrafeRight);
        SmartDashboard.putNumber("Drive Strafe Left", driveStrafeLeft);
        SmartDashboard.putBoolean("Strafe Deployed", driveBaseSystem.getStrafeState());
        SmartDashboard.putNumber("IMU Angle", robotAngle);
        SmartDashboard.putNumber("Rotate Adjust L", lAdjust);
        SmartDashboard.putNumber("Rotate Adjust R", rAdjust);
    }
    
    @Override
    public void testInit()
    {
        
    }
    
    @Override
    public void testPeriodic()
    {
        double driveLeft, driveRight, driveStrafeLeft, driveStrafeRight, robotAngle, climbSpeed, armSpeed;
        double rotateAdjust, lAdjust, rAdjust;
        
        Scheduler.getInstance().run();
        
        driveLeft = oi.getDriverJoystickValue(Ports.OIDriverLeftDrive, true);
        driveRight = oi.getDriverJoystickValue(Ports.OIDriverRightDrive, true);
        driveStrafeRight = oi.getDriverJoystickValue(Ports.OIDriverStrafeRight, true);
        driveStrafeLeft = oi.getDriverJoystickValue(Ports.OIDriverStrafeLeft, true);
        climbSpeed = oi.getOperatorJoystickValue(Ports.OIOperatorClimbWinch, false);
        armSpeed = oi.getOperatorJoystickValue(Ports.OIOperatorArm, false);
        

        robotAngle = driveBaseSystem.driveIMU.updatePeriodic();

        rotateAdjust = driveBaseSystem.getStrafeRotateAdjust();
        lAdjust = rotateAdjust / 2;
        rAdjust = -lAdjust;
        
        driveBaseSystem.setLeftSpeed(driveLeft);
        driveBaseSystem.setRightSpeed(driveRight);
        driveBaseSystem.setStrafeSpeed(driveStrafeRight, driveStrafeLeft);

        climbSystem.setWinchSpeed(climbSpeed);
        armSystem.periodic(armSpeed);
        
        SmartDashboard.putData("Deploy strafe", new StrafeDeploy());
        SmartDashboard.putData("Stow strafe", new StrafeStow());
        SmartDashboard.putData("Shift Gears up", new GearShiftCommand(true));
        SmartDashboard.putData("Shift Gears down", new GearShiftCommand(false));
        SmartDashboard.putData("Turn intake on and inwards", new IntakeCommand(true, true));
        SmartDashboard.putData("Turn intake on and outwards", new IntakeCommand(true, false));
        SmartDashboard.putData("Turn intake off", new IntakeCommand(false, false));
        SmartDashboard.putData("Toggle intake swing", new IntakeSwingToggle());
        
        SmartDashboard.putNumber("Left Encoder Raw", driveBaseSystem.getLeftEncoderRaw());
        SmartDashboard.putNumber("Right Encoder Raw", driveBaseSystem.getRightEncoderRaw());
        SmartDashboard.putNumber("Left Encoder Rate", driveBaseSystem.getLeftEncoderRate());
        SmartDashboard.putNumber("Right Encoder Rate", driveBaseSystem.getRightEncoderRate());
        SmartDashboard.putData("Auto mode", chooser);
        SmartDashboard.putNumber("Drive Left Raw", driveLeft);
        SmartDashboard.putNumber("Drive Right Raw", driveRight);
        SmartDashboard.putNumber("Drive Strafe Right", driveStrafeRight);
        SmartDashboard.putNumber("Drive Strafe Left", driveStrafeLeft);
        SmartDashboard.putBoolean("Strafe Deployed", driveBaseSystem.getStrafeState());
        SmartDashboard.putNumber("IMU Angle", robotAngle);
        SmartDashboard.putNumber("Rotate Adjust L", lAdjust);
        SmartDashboard.putNumber("Rotate Adjust R", rAdjust);
    }
}