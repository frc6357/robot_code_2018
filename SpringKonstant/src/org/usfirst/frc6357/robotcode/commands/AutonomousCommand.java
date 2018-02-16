// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

package org.usfirst.frc6357.robotcode.commands;

import org.usfirst.frc6357.robotcode.Robot;

import edu.wpi.first.wpilibj.command.Command;
//import org.usfirst.frc6357.robotcode.Robot;

/**
 *
 */
public class AutonomousCommand extends Command
{
    public AutonomousCommand()
    {

    }

    /**
     * Overloaded constructor, creates a commandgroup by parsing 2D string array for
     * data
     * 
     * @param s2d
     *            the 2D string array containing data from a csv for parsing with
     *            the format specified in CSVReader
     */
    public AutonomousCommand(String[][] s2d)
    {
        requires(Robot.driveBaseSystem);

        /*
         * Should read through the s2d for references and command types, then construct
         * a command group Format is: line[0] can be ignored, line[boo][0] is function
         * name, line[foo][1 : line[foo].length - 1] are params Afterwards, will execute
         * command group
         */
        new Thread(() ->
        {
            for (int row = 1; row < s2d.length; row++)
            {
                switch (s2d[row][0])
                {
<<<<<<< HEAD
                case "Drive":
                    System.out.println("Add driving functionality here with param: " + s2d[row][1] + " ft");
                    Robot.driveBaseSystem.setLeftSpeed(.5);
                    Robot.driveBaseSystem.setRightSpeed(.5);
                    try
                    {
                        Thread.sleep(250 * Integer.parseInt(s2d[row][1]));
                    } catch (Exception e)
                    {
                    }
                    Robot.driveBaseSystem.setLeftSpeed(0);
                    Robot.driveBaseSystem.setRightSpeed(0);
                    break;
                case "Turn":
                    System.out.println("Add turning functionality here with param: " + s2d[row][1] + " deg");
                    int parsed = Integer.parseInt(s2d[row][1]);
                    Robot.driveBaseSystem.setLeftSpeed((parsed > 0) ? .5 : -.5);
                    Robot.driveBaseSystem.setRightSpeed((parsed > 0) ? -.5 : .5);
                    try
                    {
                        Thread.sleep(250 * parsed);
                    } catch (Exception e)
                    {
                    }
                    Robot.driveBaseSystem.setLeftSpeed(0);
                    Robot.driveBaseSystem.setRightSpeed(0);
                    break;
                default:
                    System.out.println("COMMAND UNRECOGNIZED ON LINE " + row);
=======
                    case "Wait":
                        try {Thread.sleep(Integer.parseInt(s2d[row][1]));} catch(Exception e) {}
                        break;
                    case "Drive":
                        //EVENTUAL FUNCTION: driveBaseSystem.drive(s[1] in feet)
                        System.out.println("Add driving functionality here with param: " + s2d[row][1] + " ft");
                        Robot.driveBaseSystem.setLeftSpeed(.5);
                        Robot.driveBaseSystem.setRightSpeed(.5);
                        try {Thread.sleep(250 * Integer.parseInt(s2d[row][1]));} catch(Exception e) {}
                        Robot.driveBaseSystem.setLeftSpeed(0);
                        Robot.driveBaseSystem.setRightSpeed(0);
                        break;
                    case "Turn":
                        //EVENTUAL FUNCTION: driveBaseSystem.turn(s[1] is degrees)
                        System.out.println("Add turning functionality here with param: " + s2d[row][1] + " deg");
                        int parsed = Integer.parseInt(s2d[row][1]);
                        Robot.driveBaseSystem.setLeftSpeed((parsed > 0) ? .5 : -.5);
                        Robot.driveBaseSystem.setRightSpeed((parsed > 0) ? -.5 : .5);
                        try {Thread.sleep(250 * parsed);} catch(Exception e) {}
                        Robot.driveBaseSystem.setLeftSpeed(0);
                        Robot.driveBaseSystem.setRightSpeed(0);
                        break;
                    case "Arm":
                        //EVENTUAL FUNCTION: Change the height of the arm according to spec: floor, switch, scale middle, scale high
                        switch(Integer.parseInt(s2d[row][1]))
                        {
                            case 0:
                                //Floor
                                break;
                            case 1:
                                //Switch
                                break;
                            case 2:
                                //Scale
                                break;
                            case 3:
                                //High scale
                                break;
                            default:
                                System.out.println("ERROR TRYING TO PARSE ARM INPUT");
                        }
                        break;
                    case "Box Push":
                        //EVENTUAL FUNCTION: Push out the box, whatever that command ends up being called
                        break;
                    default:
                        System.out.println("COMMAND UNRECOGNIZED ON LINE " + row);
>>>>>>> fb97cc594c408298ae19283321e6099499704689
                }
            }
        }).start();
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
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end()
    {

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted()
    {

    }
}
