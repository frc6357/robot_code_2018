package org.usfirst.frc6357.robotcode.commands;

import org.usfirst.frc6357.robotcode.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class SetSlowMode extends Command {
    private boolean setter;
    public SetSlowMode() 
    {
        requires(Robot.driveBaseSystem);
    }
    
    public SetSlowMode(boolean choice)
    {
        requires(Robot.driveBaseSystem);
        setter = choice;
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
        Robot.driveBaseSystem.setSlowMode(this.setter);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
