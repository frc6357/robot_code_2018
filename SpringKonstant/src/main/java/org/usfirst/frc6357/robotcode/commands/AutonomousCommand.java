
package org.usfirst.frc6357.robotcode.commands;

import org.usfirst.frc6357.robotcode.Robot;
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
    }
    
    //Auto that waits a length of time then drives across the line
    public AutonomousCommand(long millis)
    {
    }
    
    // Overloaded Constructor to do the current plan of using the IMU and the US sensors
    public AutonomousCommand(String startSide, String override)
    {
        requires(Robot.driveBaseSystem);
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
        return true;
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