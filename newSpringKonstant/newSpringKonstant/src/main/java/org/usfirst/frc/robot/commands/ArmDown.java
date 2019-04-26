package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.subsystems.ArmSystem.ArmState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 * This class is the command to lower the pnuematics on the arm
 */
public class ArmDown extends Command {

    public ArmDown()
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
            Robot.armSystem.setArmShoulderState(Robot.armSystem.DOWN);
        }
        else
        {
            // We can't move the arm in either direction unless the intake is
            // in the up position so force it up before moving.
            Robot.armSystem.setArmElbowState(Robot.armSystem.UP);
            Robot.armSystem.setArmShoulderState(Robot.armSystem.DOWN);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished()
    {
        return (Robot.armSystem.getArmShoulderState() == ArmState.DOWN);
    }

    // Called once after isFinished returns true
    protected void end()
    {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
