package org.usfirst.frc6357.robotcode.commands;

import org.usfirst.frc6357.robotcode.Robot;
import org.usfirst.frc6357.robotcode.subsystems.ArmSystem.ArmState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This is the command for raising the pnuematics on the arm
 */
public class ArmUp extends Command
{
    public ArmUp()
    {
        requires(Robot.armSystem);
    }

    // Called just before this Command runs the first time
    protected void initialize()
    {

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute()
    {
        if(!DriverStation.getInstance().isAutonomous() &&
           (DriverStation.getInstance().getMatchTime() < 30.0))
        {
            // If we are in the end-game period, we can violate the 16" extension
            // rule so we don't need to enforce that the intake is in the UP position
            // when the arm is moving.
            Robot.armSystem.setArmShoulderState(Robot.armSystem.UP);
        }
        else
        {
            // We can't move the arm in either direction unless the intake is
            // in the up position so force it up before moving.
            Robot.armSystem.setArmElbowState(Robot.armSystem.UP);
            Robot.armSystem.setArmShoulderState(Robot.armSystem.UP);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished()
    {
        return (Robot.armSystem.getArmShoulderState() == ArmState.UP);
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
