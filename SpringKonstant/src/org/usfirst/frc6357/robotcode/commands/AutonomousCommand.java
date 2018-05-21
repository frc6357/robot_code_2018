
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
    
    //Auto that waits a length of time then drives across the line
    public AutonomousCommand(long millis)
    {
        try {Thread.sleep(millis);}
        catch(Exception e) {}
        Robot.driveBaseSystem.driveTimeDistance(143);
    }
    
    // Overloaded Constructor to do the current plan of using the IMU and the US sensors
    public AutonomousCommand(String startSide, String override)
    {
        requires(Robot.driveBaseSystem);
        
        try {Thread.sleep(100);} //Waits .1 seconds for the high gear to deploy
        catch(Exception e) {}
        
        switch(override)
        {
        case "N": //NULL OVERRIDE; SCALE FIRST, THEN SWITCH, THEN DRIVE
            if(AutoPositionCheck.getScale().equals(startSide))
                scalePlan(startSide);
            else if(AutoPositionCheck.getAllySwitch().equals(startSide))
                switchPlan(startSide);
            else
                Robot.driveBaseSystem.driveTimeDistance(143);
            break;
        case "F": //FLIP OVERRIDE; SWITCH FIRST, THEN SCALE, THEN DRIVE
            if(AutoPositionCheck.getAllySwitch().equals(startSide))
                switchPlan(startSide);
            else if(AutoPositionCheck.getScale().equals(startSide))
                scalePlan(startSide);
            else
                Robot.driveBaseSystem.driveTimeDistance(143);
            break;
        case "SO": //SWITCH ONLY OVERRIDE; SWITCH FIRST, THEN DRIVE
            if(AutoPositionCheck.getAllySwitch().equals(startSide))
            {
                switchPlan(startSide);
                break;
            }
        case "SCO": //SCALE ONLY OVERRIDE; SCALE FIRST, THEN DRIVE
            if(AutoPositionCheck.getScale().equals(startSide))
            {
                scalePlan(startSide);
                break;
            }
        default: //DRIVE ONLY OVERRIDE
            Robot.driveBaseSystem.driveTimeDistance(143);
        }
    }
    
    /**
     * Method which uses timers to make the robot drive straight to the scale, turn, and place
     * @param startSide The String representing which side the robot starts on
     */
    private void scalePlan(String startSide)
    {
        Robot.driveBaseSystem.driveTimeDistance(303); //Drive to center of scale, timed
        Robot.driveBaseSystem.turnDegrees(startSide.equals("R") ? 90 : -90); //Turn away from center
        Robot.driveBaseSystem.setLeftSpeed(-.3);
        Robot.driveBaseSystem.setRightSpeed(-.3);
        try {Thread.sleep(250);}
        catch(Exception e) {}
        Robot.driveBaseSystem.setLeftSpeed(0);
        Robot.driveBaseSystem.setRightSpeed(0);
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
    
    /**
     * Method that uses timers to make the robot drive to the switch, turn, back up, and place
     * @param startSide the String representing the side on which the robot starts
     */
    private void switchPlan(String startSide)
    {
        Robot.driveBaseSystem.driveTimeDistance(143); //Drive to center of scale, timed
        Robot.driveBaseSystem.turnDegrees(startSide.equals("R") ? 90 : -90); //Turn away from center
        Robot.driveBaseSystem.setLeftSpeed(-.4);
        Robot.driveBaseSystem.setRightSpeed(-.4);
        try {Thread.sleep(1000);}
        catch(Exception e) {}
        Robot.driveBaseSystem.setLeftSpeed(0);
        Robot.driveBaseSystem.setRightSpeed(0);
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