
package org.usfirst.frc6357.robotcode.commands;

import org.usfirst.frc6357.robotcode.Robot;
import org.usfirst.frc6357.robotcode.tools.AutoPositionCheck;

import edu.wpi.first.wpilibj.command.Command;

/**
 * This class is the class that is called during the Autonomous period The end goal of this class is to have it take in data that has been
 * parsed from CSV or Excel sheets in the CSVReader class, and then use this data to generate anonymous autonomous commands; this allows for
 * quick changes and lots of plans.
 */
public class AutonomousCommand extends Command
{
    private final double inchesPerSecond = 43;
    
    // Default Constructor
    public AutonomousCommand()
    {
        requires(Robot.driveBaseSystem);
        boolean useTimedMethod = true; // True if using old method of simply
                                       // driving for a period of time


        if (useTimedMethod)
        {
            new Thread(() -> // Thread which simply powers motors for a brief period of time
            {
                Robot.driveBaseSystem.setLeftSpeed(.4);
                Robot.driveBaseSystem.setRightSpeed(.4);
                try
                {
                    Thread.sleep(4360);
                }
                catch (Exception e)
                {

                }
                Robot.driveBaseSystem.setLeftSpeed(0);
                Robot.driveBaseSystem.setRightSpeed(0);
            }).start();
        }
        else // If not using old method, then begin using the new plan
        {
            //If the robot is started on the same side as our team's switch
            if(Robot.chooserStart.getSelected() == AutoPositionCheck.getAllySwitch())
            {
                //Drive forward 3 feet to get room to turn around
                Robot.driveBaseSystem.driveEncoderDistance(36);
                Robot.driveBaseSystem.turnDegrees(180); //Spin around
                //Drive backwards until you reach the switch (8 feet 10 inches)
                Robot.driveBaseSystem.driveEncoderDistance(-106, -.5);
//                Robot.armSystem.armElbowUp(); //Lift elbow, just in case
//                Robot.armSystem.armUp(); //Lift arm with pneumatics
//                Robot.intakeSystem.openIntakeGripper(); //Open grippers
//                Robot.intakeSystem.setIntakeSpeed(-.6, -.6); //Set the gripper motors to reverse (Spit out)
//                try {Thread.sleep(250);} catch(Exception e) {} //Wait a fourth of a second
//                Robot.intakeSystem.setIntakeSpeed(0, 0); //Stop the motors
//                Robot.intakeSystem.closeIntakeGripper(); //Close the grippers
//                Robot.armSystem.armDown(); //Lower the arm
            }
            else //The robot is on the wrong side or in the middle
            {
                //Drive forwards 10 feet to cross the auto line
                Robot.driveBaseSystem.driveEncoderDistance(120);
            }
        }
    }
    
    //Quick recreation of the auto command with the new files
    public AutonomousCommand(String[][] s2d)
    {
        requires(Robot.driveBaseSystem);
        boolean useEncoders = false; //TRUE IF ENCODERS ARE USED FOR DISTANCES
        if(useEncoders)
        {
            new Thread(() -> 
            {
                for(int row = 0; row < s2d.length; row++)
                {
                    switch(s2d[row][0])
                    {
                    case "Wait":
                        try
                        {
                            Thread.sleep(Integer.parseInt(s2d[row][1]));
                        }
                        catch(Exception e)
                        {
                        }
                        break;
                    case "Drive":
                        Robot.driveBaseSystem.driveEncoderDistance(Double.parseDouble(s2d[row][1]));
                        break;
                    case "Turn":
                        Robot.driveBaseSystem.turnDegrees(Double.parseDouble(s2d[row][1]));
                        break;
                    case "Arm":
                        if(s2d[row][1].equals("Up"))
                        {
                            Robot.armSystem.setArmElbowState(Robot.armSystem.UP);
                            Robot.armSystem.setArmShoulderState(Robot.armSystem.UP);
                        }
                        else if(s2d[row][1].equals("Down"))
                        {
                            Robot.armSystem.setArmElbowState(Robot.armSystem.UP);
                            Robot.armSystem.setArmShoulderState(Robot.armSystem.DOWN);
                        }
                        break;
                    case "Box Push":
                        Robot.intakeSystem.setIntakeSpeed(-.5, -.5);
                        Robot.intakeSystem.setIntakeGrippers(true);
                        break;
                    }
                }
            }).start();
        }
        else //CASE FOR USING TIMED METHODS
        {
            new Thread(() -> 
            {
                for(int row = 0; row < s2d.length; row++)
                {
                    switch(s2d[row][0])
                    {
                    case "Wait":
                        try
                        {
                            Thread.sleep(Integer.parseInt(s2d[row][1]));
                        }
                        catch(Exception e)
                        {
                        }
                        break;
                    case "Drive":   //SETS SPEED TO .8 FOR CALCULATED NUMBER OF SECONDS
                        Robot.driveBaseSystem.setRightSpeed(.8);
                        Robot.driveBaseSystem.setLeftSpeed(.8);
                        try {Thread.sleep(1000 * (long)(Integer.parseInt(s2d[row][1]) / inchesPerSecond));}
                        catch(Exception e) {}
                        Robot.driveBaseSystem.setRightSpeed(0);
                        Robot.driveBaseSystem.setLeftSpeed(0);
                        break;
                    case "Turn":    //USES IMU TO MEASURE TURNING REQUISITES
                        Robot.driveBaseSystem.turnDegrees(Double.parseDouble(s2d[row][1]));
                        break;
                    case "Arm":    //LIFT OR LOWER ARM, WAIT FOR 2 SECONDS TO LIFT
                        if(s2d[row][1].equals("Up"))
                        {
                            Robot.armSystem.setArmElbowState(Robot.armSystem.UP);
                            Robot.armSystem.setArmShoulderState(Robot.armSystem.UP);
                        }
                        else if(s2d[row][1].equals("Down"))
                        {
                            Robot.armSystem.setArmElbowState(Robot.armSystem.UP);
                            Robot.armSystem.setArmShoulderState(Robot.armSystem.DOWN);
                        }
                        try {Thread.sleep(2800);}
                        catch(Exception e) {}
                        break;
                    case "Box Push":
                        Robot.intakeSystem.setIntakeSpeed(1, 1);
                        Robot.intakeSystem.setIntakeGrippers(true);
                        try{Thread.sleep(750);}
                        catch(Exception e) {}
                        Robot.intakeSystem.setIntakeSpeed(0, 0);
                        Robot.intakeSystem.setIntakeGrippers(false);
                        break;
                    }
                }
            }).start();
        }
    }

    /**
     * Overloaded constructor, creates a commandgroup by parsing 2D string array for data
     *
     * @param s2d
     *            the 2D string array containing data from a csv for parsing with the format specified in CSVReader
     */
/*     public AutonomousCommand(String[][] s2d)
     {
         requires(Robot.driveBaseSystem);
        
         //Robot.driveBaseSystem.deployStrafe(true);
        
         // TODO: Consider reworking this to use a real state machine updated in the
         // execute() method. I suspect that's what you're supposed to do rather than
         // spawning an independent thread (but I could be completely wrong).
        
         /*
         * Should read through the s2d for references and command types, then  construct
         * a command group Format is: line[0] can be ignored, line[boo][0] is function
         * name, line[foo][1 : line[foo].length - 1] are params Afterwards, will
         * execute command group
         
         new Thread(() -> {
             final boolean turnByTime = false;
             final double turnSpeed = 0.3;
            
             for (int row = 1; row < s2d.length; row++)
             {
                 switch (s2d[row][0])
                 {
                 case "Wait":
                 try
                 {
                 Thread.sleep(Integer.parseInt(s2d[row][1]));
                 }
                 catch (Exception e)
                 {
                 }
                 break;
                 case "Drive":
                 Robot.driveBaseSystem.setPositionMode();
                 Robot.driveBaseSystem.enable();
                 Robot.driveBaseSystem.driveStraight(Double.parseDouble(s2d[row][1]));
                 break;
                 case "Turn":
                
                 Robot.driveBaseSystem.disable();
                 // TODO: Debug the turn by angle code below! If you want to disable it,
                 // set turnByTime to false above.
                 if (turnByTime)
                 {
                 System.out.println("Add turning functionality here with param: " +
                 s2d[row][1] + " deg");
                 int parsed = Integer.parseInt(s2d[row][1]);
                 Robot.driveBaseSystem.setLeftSpeed((parsed > 0) ? .5 : -.5);
                 Robot.driveBaseSystem.setRightSpeed((parsed > 0) ? -.5 : .5);
                 try
                 {
                 Thread.sleep(250 * parsed);
                 }
                 catch (Exception e)
                 {
                 }
                 Robot.driveBaseSystem.setLeftSpeed(0);
                 Robot.driveBaseSystem.setRightSpeed(0);
                 }
                 else
                 {
                 // Turn by angle using the IMU to measure where we are.
                 double startAngle, turnAngle, robotAngle, turnedAngle;
                 boolean turnClockwise;
                 double direction;
                
                 // Figure out where we need to start and end. Note that we
                 // need to deal with the fact that the angle will wrap from
                 // 180.0 to -180.0! To make things a bit easier to handle, we
                 // add 180 to the angle to give us a reading in the 0-360 range.
                 startAngle = Robot.driveBaseSystem.driveIMU.getRawAngle() + 180.0;
                 turnAngle = Double.parseDouble(s2d[row][1]);
                 turnedAngle = 0.0;
                
                 if (turnAngle < 0.0)
                 {
                 // Counter clockwise. Convert our start angle so we always
                 // look for increasing angles in the math later.
                 turnClockwise = false;
                 startAngle = 360 - startAngle;
                 direction = -1.0;
                 }
                 else
                 {
                 // Clockwise.
                 turnClockwise = true;
                 direction = 1.0;
                 }
                
                 turnAngle = Math.abs(turnAngle);
                
                 Robot.driveBaseSystem.setLeftSpeed(turnSpeed * direction);
                 Robot.driveBaseSystem.setRightSpeed(-(turnSpeed * direction));
                
                 while (turnedAngle < turnAngle)
                 {
                 robotAngle = Robot.driveBaseSystem.driveIMU.getRawAngle() + 180.0;
                
                 // If we're turning counter-clockwise, reverse the angle so that everything
                 // seems to be moving clockwise.
                 if (turnClockwise == false)
                 {
                 robotAngle = 360.0 - robotAngle;
                 }
                
                 // Handle the wrap case. This will occur whenever the measured angle is
                 lower
                 // than the start angle (because we adjusted everything for clockwise
                 rotations).
                 if (robotAngle < startAngle)
                 {
                 turnedAngle = (360.0 - startAngle) + robotAngle;
                 }
                 else
                 {
                 turnedAngle = robotAngle - startAngle;
                 }
                 }
                
                 Robot.driveBaseSystem.setLeftSpeed(0);
                 Robot.driveBaseSystem.setRightSpeed(0);
                 }
                 break;
                 case "Arm":
                 switch (Integer.parseInt(s2d[row][1]))
                 {
                 case 0:
                 Robot.armSystem.setArmSpeed(-0.75);
                 try
                 {
                 Thread.sleep(Ports.floorTime);
                 }
                 catch (Exception e)
                 {
                 }
                 Robot.armSystem.setArmSpeed(0);
                 break;
                 case 1:
                 Robot.armSystem.setArmSpeed(0.75);
                 try
                 {
                 Thread.sleep(Ports.switchTime);
                 }
                 catch(Exception e)
                 {
                 }
                 Robot.armSystem.setArmSpeed(0);
                 break;
                 case 2:
                 Robot.armSystem.setArmSpeed(0.75);
                 try
                 {
                 Thread.sleep(Ports.midScaleTime);
                 }
                 catch(Exception e)
                 {
                 }
                 Robot.armSystem.setArmSpeed(0);
                 break;
                 case 3:
                 Robot.armSystem.setArmSpeed(0.75);
                 try
                 {
                 Thread.sleep(Ports.highScaleTime);
                 }
                 catch(Exception e)
                 {
                 }
                 Robot.armSystem.setArmSpeed(0);
                 break;
                 default:
                 System.out.println("ERROR TRYING TO PARSE ARM INPUT");
                 }
                 break;
                 case "Box Push":
                 Robot.intakeSystem.setIntakeDown();
                 Robot.intakeSystem.setIntakeSpeed(-1, 0);
                 try
                 {
                 Thread.sleep(2000);
                 }
                 catch (Exception e)
                 {
                 }
                 Robot.intakeSystem.setIntakeSpeed(0, 0);
                 Robot.intakeSystem.setIntakeUp();
                 break;
                 }
             }
         }).start();
        
     } */

    // Called just before this Command runs the first time
    @Override
    protected void initialize()
    {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute()
    {

    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished()
    {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end()
    {

    }

    // Called when another command which requires one or more of the same subsystems is scheduled to run
    @Override
    protected void interrupted()
    {

    }
}
