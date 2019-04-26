package frc.robot.commands;

import frc.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

/**
 * The StrafeStow class implements the command which will cause the drive system
 * to stow the strafing (sideways translation) system.
 */
public class StrafeStow extends Command
{

    public StrafeStow()
    {
        requires(Robot.driveBaseSystem);
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
        Robot.driveBaseSystem.deployStrafe(false);
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
