package org.usfirst.frc6357.robotcode.commands;

import org.usfirst.frc6357.robotcode.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * A class supporting switching the drivetrain between low and high gear.
 */
public class GearShiftCommand extends Command
{

    private boolean SelectHigh;

    public GearShiftCommand(boolean high)
    {
        requires(Robot.driveBaseSystem);
        SelectHigh = high;
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
        Robot.driveBaseSystem.setHighGear(SelectHigh);
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
