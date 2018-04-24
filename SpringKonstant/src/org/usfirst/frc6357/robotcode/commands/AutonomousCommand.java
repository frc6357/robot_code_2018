
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