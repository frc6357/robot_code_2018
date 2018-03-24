package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import org.usfirst.frc6357.robotcode.Ports;

/**
 * Subsystem controlling the arm of the robot. This comprises a 2 joint arm
 * controlled by pneumatics. The "elbow" controls the angle of the intake
 * mechanism. This must be in the up position whenever the arm is raised to
 * ensure that the robot does not violate the rule that no part may extend
 * more than 16" past the robot boundary. An override is provided to allow for
 * end-of-game climb mechanism deployment where the 16" rule does not apply.
 *
 * We assume that the ARM is wired with upper and lower limit switches that we
 * use to detect when the arm finishes its main, "shoulder" movement. An override
 * is supplied to remove this logic from the code in case the switches are
 * incorrectly positioned or do not work for some other reason.
 *
 */
public class ArmSystem extends Subsystem
{
    public enum ArmState { UP, DOWN, IN_MOTION };

    private ArmState ShoulderState;
    private ArmState ElbowState;

    private final DoubleSolenoid armShoulderSolenoid;
    private final DoubleSolenoid armElbowSolenoid;
    private boolean armShoulderSet      = false;      // false == down, true == up
    private boolean armElbowSet         = false;      // false == down, true -== up

    private final DigitalInput limitUpper;
    private final DigitalInput limitLower;

    private boolean useLimitSwitches    = true;

    // Used to debounce the limit switches. We only change ShoulderState
    // after we've read the same switch condition twice in a row.
    private boolean LastUpperPressed    = false;
    private boolean LastLowerPressed    = false;

    public final boolean UP = true;
    public final boolean DOWN = false;

    //************************************************************************
    //
    // Private methods
    //
    //************************************************************************

    // Wrap the basic DigitalInput.get() method so that we can adjust for the wiring of
    // the switch. This implementation assumes that the passed input is wired normally-open
    // so that reading true from get() means that the switch is NOT pressed whereas reading
    // false means that it IS pressed.
    //
    // Note that this function returns the raw state of the switch with the sense set so that
    // true means PRESSED and false means NOT PRESSED. This does NOT perform any debouncing.
    //
    // TODO: Is the limit switches are wired normally closed, remove the "!" operator here
    // since they will then read true when pressed and we won't need to negate the return
    // value.
    private boolean isSwitchPressed(DigitalInput input)
    {
        return !input.get();
    }

    // Command the shoulder (the main part of the arm) to start moving into the up position.
    private void armUp()
    {
        armShoulderSet = true;
        armShoulderSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    // Command the shoulder (the main part of the arm) to start moving into the down position.
    private void armDown()
    {
        armShoulderSet = false;
        armShoulderSolenoid.set(DoubleSolenoid.Value.kReverse);
    }


    // Command the elbow to move into the up position.
    private void armElbowUp()
    {
        armElbowSet = true;
        armElbowSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    // Command the elbow to move into the down position.
    private void armElbowDown()
    {
        armElbowSet = false;
        armElbowSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

    // Call this function periodically to update the state of the shoulder based on the
    // current limit switch states.
    private void armUpdateShoulderState()
    {
        boolean Pressed;

        // TODO: Update the arm shoulder state. We will likely need to debounce the
        // limit switches so this provides us an easy method of reading them periodically
        // to decide when they are definitely in one or other state.
        if(useLimitSwitches)
        {
            Pressed = isSwitchPressed(limitUpper);
            if(Pressed == LastUpperPressed)
            {
                // We read the upper limit switch as pressed twice in a row so the arm is
                // now in the fully up position.
                ShoulderState = ArmState.UP;
            }
            LastUpperPressed = Pressed;

            Pressed = isSwitchPressed(limitLower);
            if(Pressed == LastLowerPressed)
            {
                // We read the lower limit switch as pressed twice in a row so the arm is
                // now in the fully down position.
                ShoulderState = ArmState.DOWN;
            }
            LastLowerPressed = Pressed;

            // If neither switch is pressed, the arm is in motion.
            if(!LastUpperPressed && !LastLowerPressed)
            {
                ShoulderState = ArmState.IN_MOTION;
            }
        }
    }

    //************************************************************************
    //
    // Public methods
    //
    //************************************************************************
    public ArmSystem()
    {
        armShoulderSolenoid = new DoubleSolenoid(Ports.ArmShoulderPCM, Ports.ArmShoulderUp, Ports.ArmShoulderDown);
        armElbowSolenoid    = new DoubleSolenoid(Ports.ArmElbowPCM, Ports.ArmElbowUp, Ports.ArmElbowDown);

        limitUpper          = new DigitalInput(Ports.ArmLimitTop);
        limitLower          = new DigitalInput(Ports.ArmLimitBottom);

        LastUpperPressed    = isSwitchPressed(limitUpper);
        LastLowerPressed    = isSwitchPressed(limitLower);

        // Call the shoulder update function once to read the actual state of the arm.
        armUpdateShoulderState();

        // We don't expect to start with the arm in the up state but, just in case, this
        // makes sure that our internal state matches the real state.
        armShoulderSet = false;    // down
        if(ShoulderState == ArmState.UP)
        {
            armShoulderSet = true; // up
        }
        armElbowSet    = true;

        ShoulderState = getArmShoulderState();
        ElbowState    = getArmElbowState();
    }

    /*
     * Call this method from the robot's main periodic callback to update the state
     * of the arm. Here we read the current state of the limit switches, perform some
     * basic debouncing, and set the arm shoulder state accordingly.
     *
     * @returns None
     */
    public void periodic()
    {
        // Read the limit switches (if enabled) and determine the current state of the arm.
        armUpdateShoulderState();
    }

    //
    // Commands the main part of the arm (the shoulder) to move into the upward or downward
    // position. This method returns immediately. To determine when the arm has reached the
    // extent of its movement, use getArmShoulderState() and wait for it to return a value other
    // than ArmState.IN_MOTION (assuming limit switches are in use).
    //
    // @param Up must be set to true to move the arm to its up position or false to move it to
    // the down position.
    //
    public void setArmShoulderState(boolean Up)
    {
        if(Up)
        {
            armUp();
        }
        else
        {
            armDown();
        }
    }

    //
    // Returns the current state of the arm shoulder (the main section of the arm). If limit
    // switches are in use, this will report the current actual state of the arm. If they are not,
    // we merely return the last commanded state.
    //
    // @returns ArmState.UP if the upper limit switch is pressed or, if limit switches are disabled,
    //          if the elbow was last commanded to be in the up position.
    //          ArmState.DOWN if the lower limit switch is pressed or, if limit switches are disabled,
    //          if the shoulder was last commanded to be in the down position.
    //          ArmState.IN_MOTION if limit switches are in use and neither the upper nor lower
    //          switch is pressed.
    //
    public ArmState getArmShoulderState()
    {
        // If we're using the limit switches, the state is updated periodically via the
        // periodic() method so we just return the last state we determined.
        if(!useLimitSwitches)
        {
            // We are not using the main arm limit switches so just return
            // the current commanded position.
            if(armShoulderSet)
            {
                ShoulderState = ArmState.UP;
            }
            else
            {
                ShoulderState = ArmState.DOWN;
            }
        }

        return ShoulderState;
    }

    //
    // Commands the lower joint of the arm (the elbow) to move into the upward or downward
    // position. This method returns immediately. To determine when the elbow has reached the
    // extent of its movement, use getArmElbowrState() and wait for it to return a value other
    // than ArmState.IN_MOTION.
    //
    // @param Up must be set to true to move the elbow to its up position or false to move it to
    // the down position.
    //
    public void setArmElbowState(boolean Up)
    {
        if(Up)
        {
            armElbowUp();
        }
        else
        {
            armElbowDown();
        }
    }

    //
    // Returns the current state of the arm elbow. Because the robot (currently) has no
    // feedback on this joint, we merely return the last commanded state.
    //
    // @returns ArmState.UP if the elbow was last commanded to be in the up position.
    //          ArmState.DOWN if the elbow was last commanded to be in the down position.
    //
    public ArmState getArmElbowState()
    {
        // We have no limit switches to sense the position of the
        // elbow so just return the current commanded position.
        if(armElbowSet)
        {
            ElbowState = ArmState.UP;
        }
        else
        {
            ElbowState = ArmState.DOWN;
        }

        return ElbowState;
    }
    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
