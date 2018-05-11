package org.usfirst.frc6357.robotcode.commands;

import org.usfirst.frc6357.robotcode.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *This class sets up the command for what the constructors for the intake wants to do
 */
public class IntakeCommand extends Command
{
    private final boolean isStart;
    private final boolean isIn;
    
    /**
     * 
     * @param start: sets the state of the robot when the button is pressed
     * @param in: sets the state of whether the cube is in or not
     */
    public IntakeCommand(boolean start, boolean in)
    {
        requires(Robot.intakeSystem);
        isStart = start;
        isIn = in;
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished()
    {
        return true;
    }

    // Called once after isFinished returns true
    protected void end()
    {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted()
    {
    }
}
