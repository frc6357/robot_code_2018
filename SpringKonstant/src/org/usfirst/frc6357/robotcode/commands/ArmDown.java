package org.usfirst.frc6357.robotcode.commands;

import org.usfirst.frc6357.robotcode.Robot;
import org.usfirst.frc6357.robotcode.subsystems.ArmSystem.ArmState;

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
        if(!DriverStation.getInstance().isAutonomous())
        {
            // If we are in the end-game period, we can violate the 16" extension
            // rule so we don't need to enforce that the intake is in the UP position
            // when the arm is moving.
            if(DriverStation.getInstance().getMatchTime() < 30.0)
            {
                Robot.armSystem.setArmShoulderState(Robot.armSystem.DOWN);
            }
            else
            {
                // TODO: This is wrong. As coded, it will force the intake into
                // the down position whenever we move the arm down. This is guaranteed
                // to violate game rules!
                Robot.armSystem.setArmElbowState(Robot.armSystem.DOWN);
                Robot.armSystem.setArmShoulderState(Robot.armSystem.DOWN);
            }
        }
        else
        {
            // TODO: It autonomous, this forces the intake into the DOWN position whenever
            // we start lowering the arm. This is guaranteed to violate the 16" extension
            // rule!
            Robot.armSystem.setArmElbowState(Robot.armSystem.DOWN);
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
