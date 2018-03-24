package org.usfirst.frc6357.robotcode.commands;

import org.usfirst.frc6357.robotcode.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *This class initalizes the methods to test the PID Position to collect data for autonomous
 */
public class TestPidPosition extends Command {

    public TestPidPosition() 
    {
        requires(Robot.driveBaseSystem);
    }

    // Called just before this Command runs the first time
    protected void initialize() 
    {
        Robot.driveBaseSystem.enable();
        Robot.driveBaseSystem.setPositionMode();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() 
    {
        Robot.driveBaseSystem.driveStraight(10);
    }
    
    
    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished()
    {
        return false;
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