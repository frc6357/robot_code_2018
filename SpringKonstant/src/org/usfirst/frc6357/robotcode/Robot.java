package org.usfirst.frc6357.robotcode;

import java.io.IOException;

import org.usfirst.frc6357.robotcode.commands.AutonomousCommand;
import org.usfirst.frc6357.robotcode.commands.GearShiftCommand;
import org.usfirst.frc6357.robotcode.commands.IntakeCommand;
import org.usfirst.frc6357.robotcode.commands.IntakeSwingToggle;
import org.usfirst.frc6357.robotcode.commands.StrafeDeploy;
import org.usfirst.frc6357.robotcode.commands.StrafeStow;
import org.usfirst.frc6357.robotcode.subsystems.ArmSystem;
import org.usfirst.frc6357.robotcode.subsystems.ClimbSystem;
import org.usfirst.frc6357.robotcode.subsystems.DriveBaseSystem;
import org.usfirst.frc6357.robotcode.subsystems.IntakeSystem;
import org.usfirst.frc6357.robotcode.tools.AutoPositionCheck;
import org.usfirst.frc6357.robotcode.tools.CSVReader;

import edu.wpi.first.wpilibj.DriverStation;
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
    SendableChooser<String> chooserStart = new SendableChooser<>(); // Allows the user to pick the starting point
    SendableChooser<String> chooserEnd = new SendableChooser<>(); // Allows the user to pick the ending point
    SendableChooser<String> chooserAlt = new SendableChooser<>();// Allows the user to select alternate path if available
      
    // Subsystems
    public static DriveBaseSystem driveBaseSystem;
    public static ClimbSystem climbSystem;
    public static ArmSystem armSystem;
    public static IntakeSystem intakeSystem;

    public static OI oi;

    /**
     * This function is run when the robot is first started up and should be used for any initialization code.
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

        chooserStart.addDefault("Middle", "M");
        chooserStart.addObject("Left", "L");
        chooserStart.addObject("Right", "R");

        chooserEnd.addDefault("Drive straight", "Drive");
        chooserEnd.addObject("Switch", "Switch");
        chooserEnd.addObject("Scale", "Scale");

        chooserAlt.addDefault("No", "False");
        chooserAlt.addObject("Yes", "True");

    }

    /**
     * This function is called when the disabled button is hit. You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit()
    {
        driveBaseSystem.deployStrafe(false);
        driveBaseSystem.leftEncoder.reset();
        driveBaseSystem.rightEncoder.reset();
    }

    /**
     * This function is called continually while the robot is disabled.
     */
    @Override
    public void disabledPeriodic()
    {
        Scheduler.getInstance().run();
        SmartDashboard.putNumber("Distance Per Pulse", driveBaseSystem.leftEncoder.getDistancePerPulse());
        SmartDashboard.putNumber("Distance (getDistance())", driveBaseSystem.leftEncoder.getDistance());
        SmartDashboard.putNumber("Raw value", driveBaseSystem.leftEncoder.get());
        SmartDashboard.putNumber("Rate", driveBaseSystem.leftEncoder.getRate());
    }

    /**
     * This function is called when the autonomous period begins
     */
    @Override
    public void autonomousInit()
    {
        // intakeSystem.setIntakeUp();
        // intakeSystem.setIntakeGrippers(false);
        driveBaseSystem.leftEncoder.reset(); // Reset encoder distances to zero
        driveBaseSystem.rightEncoder.reset();
        driveBaseSystem.deployStrafe(false); // Lift Strafe
        intakeSystem.setIntakeGrippers(false); // Close gripper

        autonomousCommand = new AutonomousCommand(); // Select new autoplan
        // Currently unused code which would parse CSV files.
        /*
         * try { autonomousCommand = new AutonomousCommand(CSVReader.parse(getSelectedFile())); } catch (IOException e) {
         * System.out.println("Exception here: " + e); }
         */

        // autonomousCommand = new TestPidPosition();
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * Method which parses through the various choosers and figures out which auto-plan to use Should allow the user to have a simple way to
     * select from lots of plans Deprecated due to CSV files not currently working.
     *
     * @return the String which represents the name of the file to parse
     */
    @Deprecated
    private String getSelectedFile()
    {
        AutoPositionCheck.getGameData();

        if (chooserEnd.getSelected().equals("Drive")) { return "/home/lvuser/AutoSheets/CSV13.csv"; }
        switch (chooserStart.getSelected())
        {
            case "L":
                switch (chooserEnd.getSelected())
                {
                    case "Switch":
                        if (AutoPositionCheck.getAllySwitch().equals("L")) return "/home/lvuser/AutoSheets/CSV1.csv";
                        else
                        {
                            if (chooserAlt.getSelected().equals("False")) return "/home/lvuser/AutoSheets/CSV2.csv";
                            else return "/home/lvuser/AutoSheets/CSV9.csv";
                        }
                    case "Scale":
                        if (AutoPositionCheck.getScale().equals("L")) return "/home/lvuser/AutoSheets/CSV3.csv";
                        else
                        {
                            if (chooserAlt.getSelected().equals("False")) return "/home/lvuser/AutoSheets/CSV4.csv";
                            else return "/home/lvuser/AutoSheets/CSV10.csv";
                        }
                }
            case "R":
                switch (chooserEnd.getSelected())
                {
                    case "Switch":
                        if (AutoPositionCheck.getAllySwitch().equals("R")) return "/home/lvuser/AutoSheets/CSV6.csv";
                        else
                        {
                            if (chooserAlt.getSelected().equals("False")) return "/home/lvuser/AutoSheets/CSV5.csv";
                            else return "/home/lvuser/AutoSheets/CSV11.csv";
                        }
                    case "Scale":
                        if (AutoPositionCheck.getScale().equals("L"))
                        {
                            if (chooserAlt.getSelected().equals("False")) return "/home/lvuser/AutoSheets/CSV3.csv";
                            else return "/home/lvuser/AutoSheets/CSV12.csv";
                        }
                        else return "/home/lvuser/AutoSheets/CSV4.csv";
                }
        }
        return "/home/lvuser/AutoSheets/Test.csv";
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic()
    {
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

        driveBaseSystem.deployStrafe(true);
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic()
    {
        double driveLeft, driveRight, driveStrafeLeft, driveStrafeRight, robotAngle, climbSpeed, armSpeed, intakeSpeed;
        double rotateAdjust, lAdjust, rAdjust, intakeSpeedIn, intakeSpeedOut;

        Scheduler.getInstance().run();

        driveLeft = oi.getDriverJoystickValue(Ports.OIDriverLeftDrive, true); // Retrieves the status of all buttons and joysticks
        driveRight = oi.getDriverJoystickValue(Ports.OIDriverRightDrive, true);
        driveStrafeRight = oi.getDriverJoystickValue(Ports.OIDriverStrafeRight, true);
        driveStrafeLeft = oi.getDriverJoystickValue(Ports.OIDriverStrafeLeft, true);
        climbSpeed = oi.getOperatorJoystickValue(Ports.OIOperatorClimbWinch, false);
        intakeSpeedIn = oi.getOperatorJoystickValue(Ports.OIOperatorIntakeIn, false);
        intakeSpeedOut = oi.getOperatorJoystickValue(Ports.OIOperatorIntakeOut, false);

        robotAngle = driveBaseSystem.driveIMU.updatePeriodic(); // Retrieve robot angle

        rotateAdjust = driveBaseSystem.getStrafeRotateAdjust();
        lAdjust = rotateAdjust / 2;
        rAdjust = -lAdjust;

        driveBaseSystem.setLeftSpeed(driveLeft); // Listens to input and drives the robot
        driveBaseSystem.setRightSpeed(driveRight);
        driveBaseSystem.setStrafeSpeed(driveStrafeRight, driveStrafeLeft);

        climbSystem.setWinchSpeed(climbSpeed);
        intakeSystem.setIntakeSpeed(intakeSpeedOut, intakeSpeedIn);

        SmartDashboard.putData("Deploy strafe", new StrafeDeploy()); // Put Commands onto the shuffleboard for testing
        SmartDashboard.putData("Stow strafe", new StrafeStow());
        SmartDashboard.putData("Shift Gears up", new GearShiftCommand(true));
        SmartDashboard.putData("Shift Gears down", new GearShiftCommand(false));
        SmartDashboard.putData("Turn intake on and inwards", new IntakeCommand(true, true));
        SmartDashboard.putData("Turn intake on and outwards", new IntakeCommand(true, false));
        SmartDashboard.putData("Turn intake off", new IntakeCommand(false, false));
        SmartDashboard.putData("Toggle intake swing", new IntakeSwingToggle());

        SmartDashboard.putNumber("Left Encoder Raw", driveBaseSystem.getLeftEncoderRaw()); // Put data onto Shuffleboard for reading
        SmartDashboard.putNumber("Right Encoder Raw", driveBaseSystem.getRightEncoderRaw());
        SmartDashboard.putNumber("Left Encoder Rate", driveBaseSystem.getLeftEncoderRate());
        SmartDashboard.putNumber("Right Encoder Rate", driveBaseSystem.getRightEncoderRate());
        SmartDashboard.putData("Auto Start point", chooserStart);
        SmartDashboard.putData("Auto End point", chooserEnd);
        SmartDashboard.putData("Use alternate (Early cross) path (if possible)?", chooserAlt);
        SmartDashboard.putNumber("Drive Left Raw", driveLeft);
        SmartDashboard.putNumber("Drive Right Raw", driveRight);
        SmartDashboard.putNumber("Drive Strafe Right", driveStrafeRight);
        SmartDashboard.putNumber("Drive Strafe Left", driveStrafeLeft);
        SmartDashboard.putBoolean("Strafe Deployed", driveBaseSystem.getStrafeState());
        SmartDashboard.putNumber("IMU Angle", robotAngle);
        SmartDashboard.putNumber("Rotate Adjust L", lAdjust);
        SmartDashboard.putNumber("Rotate Adjust R", rAdjust);

        SmartDashboard.putNumber("Encoder Distance Per Pulse", driveBaseSystem.leftEncoder.getDistancePerPulse());
        SmartDashboard.putNumber("Encoder Distance", driveBaseSystem.leftEncoder.getDistance());
    }

    /**
     * Method which is called when we begin a test
     */
    @Override
    public void testInit()
    {

    }

    /**
     * Method which is called periodaclly while testing
     */
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

        robotAngle = driveBaseSystem.driveIMU.updatePeriodic();

        rotateAdjust = driveBaseSystem.getStrafeRotateAdjust();
        lAdjust = rotateAdjust / 2;
        rAdjust = -lAdjust;

        driveBaseSystem.setLeftSpeed(driveLeft);
        driveBaseSystem.setRightSpeed(driveRight);
        driveBaseSystem.setStrafeSpeed(driveStrafeRight, driveStrafeLeft);

        climbSystem.setWinchSpeed(climbSpeed);
        armSystem.periodic();

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
        SmartDashboard.putData("Auto mode", chooserStart);
        SmartDashboard.putNumber("Drive Left Raw", driveLeft);
        SmartDashboard.putNumber("Drive Right Raw", driveRight);
        SmartDashboard.putNumber("Drive Strafe Right", driveStrafeRight);
        SmartDashboard.putNumber("Drive Strafe Left", driveStrafeLeft);
        SmartDashboard.putBoolean("Strafe Deployed", driveBaseSystem.getStrafeState());
        SmartDashboard.putNumber("IMU Angle", robotAngle);
        SmartDashboard.putNumber("Rotate Adjust L", lAdjust);
        SmartDashboard.putNumber("Rotate Adjust R", rAdjust);
    }

//    public void updateSmartDashboard()
//    {
//
//    }
}