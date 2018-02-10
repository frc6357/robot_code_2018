// OI
//
// This is the operator interface class for the 2018 Team 6357 robot.
//

package org.usfirst.frc6357.robotcode;

import org.usfirst.frc6357.robotcode.commands.*;
import org.usfirst.frc6357.robotcode.tools.FilteredJoystick;
import org.usfirst.frc6357.robotcode.tools.filters.*;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI
{
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by its isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());

    private FilteredJoystick joystickOperator;
    private FilteredJoystick joystickDriver;

    private FilterDeadband   filterClimbDeadband;

    private Button buttonIntakeIn;
    private Button buttonIntakeOut;
    private Button buttonIntakeSwing;
    private Button buttonStrafeDeploy;
    private Button buttonStrafeStow;
    private Button buttonLowGear;
    private Button buttonHighGear;

    public OI()
    {
        // Instantiate the joystick devices.
        joystickOperator = new FilteredJoystick(Ports.OIOperatorJoystick);
        joystickDriver   = new FilteredJoystick(Ports.OIDriverJoystick);

        // Create all the buttons we will be using.
        buttonIntakeIn     = new JoystickButton(joystickOperator, Ports.OIOperatorIntakeIn);
        buttonIntakeOut    = new JoystickButton(joystickOperator, Ports.OIOperatorIntakeOut);
        buttonIntakeSwing  = new JoystickButton(joystickOperator, Ports.OIOperatorIntakeSwing);

        buttonStrafeDeploy = new JoystickButton(joystickDriver, Ports.OIDriverStrafeDeploy);
        buttonStrafeStow   = new JoystickButton(joystickDriver, Ports.OIDriverStrafeStow);

        buttonLowGear      = new JoystickButton(joystickDriver, Ports.IODriverGearSelectLow);
        buttonHighGear     = new JoystickButton(joystickDriver, Ports.IODriverGearSelectHigh);

        buttonStrafeDeploy.whenPressed(new StrafeDeploy());
        buttonStrafeStow.whenPressed(new StrafeStow());

        buttonLowGear.whenPressed(new GearShiftCommand(false));
        buttonHighGear.whenPressed(new GearShiftCommand(true));

        buttonIntakeSwing.whenPressed(new IntakeSwingToggle());

        buttonIntakeIn.whenPressed(new IntakeCommand(true, true));
        buttonIntakeIn.whenReleased(new IntakeCommand(false, true));
        buttonIntakeOut.whenPressed(new IntakeCommand(true, false));
        buttonIntakeOut.whenReleased(new IntakeCommand(false, false));

        // Set deadbands and response curves for various joystick axes.
        filterClimbDeadband = new FilterDeadband(0.1);
        joystickOperator.setFilter(Ports.OIOperatorClimbWinch, filterClimbDeadband);

        // SmartDashboard insertions for autonomous command chooser.
        SmartDashboard.putData("Autonomous Command", new AutonomousCommand());
    }

    /**
     *
     * This function may be called to retrieve the current, possibly-filtered, value
     * of a given joystick axis on the driver joystick device.
     *
     * @param port
     *            The axis number for the joystick being queried. This will come
     *            from Ports.java.
     * @ param invert
     *            If true, the returned joystick value will be inverted. If false,
     *            the unmodified joystick value is returned.
     * @return returnType The value of the joystick axis. Note that this may be a
     *         filtered value if we subclass the joystick to allow control of
     *         deadbands or response curves.
     */
    public double getDriverJoystickValue(int port, boolean invert)
    {
        double multiplier = 1.0;

        if(invert)
        {
            multiplier = -1.0;
        }
        return multiplier * joystickDriver.getRawAxis(port);
    }

    /**
     *
     * This function may be called to retrieve the current, possibly-filtered, value
     * of a given joystick axis on the operator joystick device.
     *
     * @param port
     *            The axis number for the joystick being queried. This will come
     *            from Ports.java.
     * @ param invert
     *            If true, the returned joystick value will be inverted. If false,
     *            the unmodified joystick value is returned.
     * @return returnType The value of the joystick axis. Note that this may be a
     *         filtered value if we subclass the joystick to allow control of
     *         deadbands or response curves.
     */
    public double getOperatorJoystickValue(int port, boolean invert)
    {
        double multiplier = 1.0;

        if(invert)
        {
            multiplier = -1.0;
        }
        return (multiplier * joystickOperator.getRawAxis(port));
    }
}
