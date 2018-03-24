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
        if(!DriverStation.getInstance().isAutonomous())
            // In automonous and teleop, we must ensure that we do not move the arm upwards
            // while the intake is in the DOWN position. We enforce this by explicitly moving
            // the intake into the UP position as we start moving the arm. This restriction
            // does not apply if we're in the endgame period.
            if(DriverStation.getInstance().getMatchTime() < 30.0)
            {
                Robot.armSystem.setArmShoulderState(Robot.armSystem.UP);
            }
            else
            {
                Robot.armSystem.setArmElbowState(Robot.armSystem.UP);
                Robot.armSystem.setArmShoulderState(Robot.armSystem.UP);
            }
        else if(DriverStation.getInstance().isAutonomous())
        {
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
