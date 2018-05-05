package org.usfirst.frc6357.robotcode;

//import java.io.IOException;

import org.usfirst.frc6357.robotcode.commands.AutonomousCommand;
import org.usfirst.frc6357.robotcode.subsystems.ArmSystem;
import org.usfirst.frc6357.robotcode.subsystems.DriveBaseSystem;
import org.usfirst.frc6357.robotcode.subsystems.IntakeSystem;
import org.usfirst.frc6357.robotcode.subsystems.SensorSystem;
import org.usfirst.frc6357.robotcode.tools.AutoPositionCheck;

import edu.wpi.first.wpilibj.CameraServer;
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
    public static SendableChooser<String> chooserEnd = new SendableChooser<>(); // Allows the user to pick the ending point
    public static SendableChooser<String> chooserBox = new SendableChooser<>(); // Allows the user to choose to attempt to pick up a box

    // Subsystems
    public static DriveBaseSystem driveBaseSystem;
    public static SensorSystem sensorSystem;
    public static ArmSystem armSystem;
    public static IntakeSystem intakeSystem;
    //public static LEDs lights;
       
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
        sensorSystem = new SensorSystem();
        armSystem = new ArmSystem();
        intakeSystem = new IntakeSystem();
       // lights = new LEDs();
       
        // OI must be constructed after subsystems. If the OI creates Commands
        // (which it very likely will), subsystems are not guaranteed to be
        // constructed yet. Thus, their requires() statements may grab null
        // pointers. Bad news. Don't move it.
        oi = new OI();

        // Add commands to Autonomous Sendable Chooser
        // It is VITAL TO NOTE that if the user picks an illegal combination of choices then the program automatically chooses to cross the auto-line
        chooserStart.addDefault("Left", "L");
        chooserStart.addObject("Right", "R");
        chooserStart.addObject("TEST", "T");

        chooserEnd.addDefault("Auto Line", "Auto");
        chooserEnd.addObject("Null Zone", "Null");
        chooserEnd.addObject("Switch", "Switch");
        chooserEnd.addObject("Scale", "Scale");
        
        chooserBox.addDefault("Don't pick up a box", "N");
        chooserBox.addObject("Pick up a box", "Y");
        chooserBox.addObject("Test pickup", "T");
        
        CameraServer.getInstance().startAutomaticCapture();        
    }

    /**
     * This function is called when the disabled button is hit. You can use it to reset subsystems before shutting down.
     */
    @Override
    public void disabledInit()
    {
        //lights.randomColor();
        driveBaseSystem.deployStrafe(false);
        driveBaseSystem.leftEncoder.reset();
        driveBaseSystem.rightEncoder.reset();
        armSystem.armDown();
        armSystem.armElbowUp();
        intakeSystem.setIntakeGrippers(false); // Close gripper
        Robot.driveBaseSystem.setHighGear(false);
        
        SmartDashboard.putData("Start Chooser", chooserStart);
        SmartDashboard.putData("End Chooser", chooserEnd);
        SmartDashboard.putData("Box Option", chooserBox);
        
    }

    /**
     * This function is called continually while the robot is disabled.
     */
    @Override
    public void disabledPeriodic()
    {
        Scheduler.getInstance().run();
        
        driveBaseSystem.setHighGear(false);
        
        SmartDashboard.putData("Start Chooser", chooserStart);
        SmartDashboard.putData("End Chooser", chooserEnd);
        SmartDashboard.putData("Box Option", chooserBox);
    }
    
    /**
     * This function is called when the autonomous period begins
     */
    @Override
    public void autonomousInit()
    {
        driveBaseSystem.leftEncoder.reset(); // Reset encoder distances to zero
        driveBaseSystem.rightEncoder.reset();
        driveBaseSystem.deployStrafe(false); // Lift Strafe
        driveBaseSystem.setHighGear(true);

//        autonomousCommand = new AutonomousCommand(); // Select new autoplan, just drives straight
        AutoPositionCheck.getGameData();
        
        autonomousCommand = new AutonomousCommand(chooserStart.getSelected());

        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * Method which parses through the various choosers and figures out which auto-plan to use Should allow the user to have a simple way to
     * select from lots of plans Deprecated due to CSV files not currently working.
     *
     * @return the String which represents the name of the file to parse
     */
    private String getSelectedFile()
    {
        AutoPositionCheck.getGameData();
        String start = chooserStart.getSelected(), end = chooserEnd.getSelected(), box = chooserBox.getSelected();
        String scale = AutoPositionCheck.getScale(), allySwitch = AutoPositionCheck.getAllySwitch();
        
        if(start.equals("T"))
            return "/home/lvuser/AutoSheets/Test.csv";
        if(box.equals("T"))
            return "/home/lvuser/AutoSheets/Test2.csv";
        if(end.equals("Auto"))
            return "/home/lvuser/AutoSheets/CSV1.csv";
        if(end.equals("Null"))
            return scale.equals(start) ? "/home/lvuser/AutoSheets/CSV2.csv" : "/home/lvuser/AutoSheets/CSV18.csv";
        
        switch(start + end + box)
        {
            case "LSwitchN":
                return allySwitch.equals(start) ? "/home/lvuser/AutoSheets/CSV3.csv" : "/home/lvuser/AutoSheets/CSV5.csv";
            case "RSwitchN":
                return allySwitch.equals(start) ? "/home/lvuser/AutoSheets/CSV3.csv" : "/home/lvuser/AutoSheets/CSV4.csv";
            case "LScaleN":
                return scale.equals(start) ? "/home/lvuser/AutoSheets/CSV6.csv" : "/home/lvuser/AutoSheets/CSV7.csv";
            case "RScaleN":
                return scale.equals(start) ? "/home/lvuser/AutoSheets/CSV8.csv" : "/home/lvuser/AutoSheets/CSV9.csv";
            case "LSwitchY":
                return allySwitch.equals(start) ? "/home/lvuser/AutoSheets/CSV10.csv" : "/home/lvuser/AutoSheets/CSV11.csv";
            case "RSwitchY":
                return allySwitch.equals(start) ? "/home/lvuser/AutoSheets/CSV12.csv" : "/home/lvuser/AutoSheets/CSV13.csv";
            case "LScaleY":
                return scale.equals(start) ? "/home/lvuser/AutoSheets/CSV14.csv" : "/home/lvuser/AutoSheets/CSV15.csv";
            case "RScaleY":
                return scale.equals(start) ? "/home/lvuser/AutoSheets/CSV16.csv" : "/home/lvuser/AutoSheets/CSV17.csv";
        }
        
        return "/home/lvuser/AutoSheets/CSV1.csv";
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
        Robot.driveBaseSystem.setHighGear(false);
    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic()
    {
        double driveLeft, driveRight, robotAngle;
        double intakeSpeedIn, intakeSpeedOut;

        Scheduler.getInstance().run();

        driveLeft = oi.getDriverJoystickValue(Ports.OIDriverLeftDrive, true); // Retrieves the status of all buttons and joysticks
        driveRight = oi.getDriverJoystickValue(Ports.OIDriverRightDrive, true);
        intakeSpeedIn = oi.getOperatorJoystickValue(Ports.OIOperatorIntakeIn, false);
        intakeSpeedOut = oi.getOperatorJoystickValue(Ports.OIOperatorIntakeOut, false);

        robotAngle = driveBaseSystem.driveIMU.updatePeriodic(); // Retrieve robot angle

        driveBaseSystem.setLeftSpeed(driveLeft); // Listens to input and drives the robot
        driveBaseSystem.setRightSpeed(driveRight);

        intakeSystem.setIntakeSpeed(intakeSpeedOut, intakeSpeedIn);

        if(counter % 10 == 0)
        {
            SmartDashboard.putNumber("Left Encoder Raw", driveBaseSystem.getLeftEncoderRaw()); // Put data onto Shuffleboard for reading
            SmartDashboard.putNumber("Right Encoder Raw", driveBaseSystem.getRightEncoderRaw());
            SmartDashboard.putNumber("Left Encoder Rate", driveBaseSystem.getLeftEncoderRate());
            SmartDashboard.putNumber("Right Encoder Rate", driveBaseSystem.getRightEncoderRate());
            SmartDashboard.putNumber("Right Encoder Dist", driveBaseSystem.rightEncoder.getDistance());
            SmartDashboard.putNumber("Left Encoder Dist", driveBaseSystem.leftEncoder.getDistance());
            SmartDashboard.putBoolean("Strafe Deployed", driveBaseSystem.getStrafeState());
            SmartDashboard.putNumber("IMU Angle", robotAngle);
        }
        counter++;
    }
}