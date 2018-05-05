
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
    // Default Constructor; simply drives forward for 4.36 seconds
    public AutonomousCommand()
    {
        requires(Robot.driveBaseSystem);
        
        new Thread(() ->
        {
            try
            {
                Robot.driveBaseSystem.setLeftSpeed(.5);
                Robot.driveBaseSystem.setRightSpeed(.5);
                Thread.sleep(4360);
                Robot.driveBaseSystem.setLeftSpeed(0);
                Robot.driveBaseSystem.setRightSpeed(0);
            }
            catch(Exception e) {}
        }).start();
    }
    
    // Overloaded Constructor to do the current plan of using the IMU and the US sensors
    public AutonomousCommand(String startSide)
    {
        requires(Robot.driveBaseSystem);
       // requires(Robot.sensorSystem);
        
        try {Thread.sleep(100);} //Waits .1 seconds for the high gear to deploy
        catch(Exception e) {}
        
        if(AutoPositionCheck.getScale().equals(startSide)) //Same side, go to scale
        {
            Robot.driveBaseSystem.driveTimeDistance(305); //Drive to center of scale
//            Robot.sensorSystem.getDistanceToCenter(); //Ping to check position 
            Robot.driveBaseSystem.turnDegrees(startSide.equals("R") ? 90 : -90); //Turn away from center
            Robot.armSystem.armUp(); //Lift arm for 3 seconds
            try {Thread.sleep(2800);}
            catch(Exception e) {}
            Robot.intakeSystem.setIntakeGrippers(true);
            try {Thread.sleep(200);}
            catch (Exception e) {}
            Robot.intakeSystem.setIntakeSpeed(0,.5); //Open and reverse gripper
            try {Thread.sleep(1000);}
            catch (Exception e) {}
            Robot.intakeSystem.setIntakeSpeed(0, 0); //Close and stop gripper
            Robot.intakeSystem.setIntakeGrippers(false);
            Robot.armSystem.armDown(); //Lower the arm
        }
        else if(AutoPositionCheck.getAllySwitch().equals(startSide)) //Same side, go to switch
        {
        	Robot.driveBaseSystem.driveTimeDistance(140); //Drive to center of scale
//          Robot.sensorSystem.getDistanceToCenter(); //Ping to check position 
	        Robot.driveBaseSystem.turnDegrees(startSide.equals("R") ? 90 : -90); //Turn away from center
	        Robot.armSystem.armUp(); //Lift arm for 3 seconds
	        try {Thread.sleep(2800);}
	        catch(Exception e) {}
	        Robot.intakeSystem.setIntakeGrippers(true);
	        try {Thread.sleep(200);}
	        catch (Exception e) {}
	        Robot.intakeSystem.setIntakeSpeed(0,.5); //Open and reverse gripper
	        try {Thread.sleep(1000);}
	        catch (Exception e) {}
	        Robot.intakeSystem.setIntakeSpeed(0, 0); //Close and stop gripper
	        Robot.intakeSystem.setIntakeGrippers(false);
	        Robot.armSystem.armDown(); //Lower the arm
        }
        else //Drive forward
        {
            Robot.driveBaseSystem.driveTimeDistance(300); //Drive 25'
        }
    }
    
    //Quick recreation of the auto command with the new files
    public AutonomousCommand(String[][] s2d)
    {
        requires(Robot.driveBaseSystem);

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
                    //Find actually viable drive method
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
                    try {Thread.sleep(2800);}
                    catch(Exception e) {}
                    break;
                case "Wrist":
                    if(s2d[row][1].equals("Up"))
                        Robot.armSystem.setArmElbowState(Robot.armSystem.UP);
                    else if(s2d[row][1].equals("Down"))
                        Robot.armSystem.setArmElbowState(Robot.armSystem.DOWN);
                    try {Thread.sleep(500);}
                    catch(Exception e) {}
                    break;
                case "Grippers":
                    if(s2d[row][1].equals("Open"))
                    {
                        Robot.intakeSystem.setIntakeGrippers(true);
                        Robot.intakeSystem.setIntakeSpeed(.5, .5);
                    }
                    else if(s2d[row][1].equals("Close"))
                    {
                        Robot.intakeSystem.setIntakeGrippers(false);
                        Robot.intakeSystem.setIntakeSpeed(0, 0);
                    }
                    break;
                case "Box Push":
                    Robot.intakeSystem.setIntakeSpeed(-.5, -.5);
                    Robot.intakeSystem.setIntakeGrippers(true);
                    try {Thread.sleep(250);}
                    catch (Exception e) {}
                    Robot.intakeSystem.setIntakeSpeed(0, 0);
                    Robot.intakeSystem.setIntakeGrippers(false);
                    break;
                }
            }
        }).start();
    }

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