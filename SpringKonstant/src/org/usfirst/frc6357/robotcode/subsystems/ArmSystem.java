package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;

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

    public ArmSystem()
    {
        armShoulderSolenoid = new DoubleSolenoid(Ports.ArmShoulderPCM, Ports.ArmShoulderUp, Ports.ArmShoulderDown);
        armElbowSolenoid    = new DoubleSolenoid(Ports.ArmElbowPCM, Ports.ArmElbowUp, Ports.ArmElbowDown);

        limitUpper          = new DigitalInput(Ports.ArmLimitTop);
        limitLower          = new DigitalInput(Ports.ArmLimitBottom);

        LastUpperPressed    = isSwitchPressed(limitUpper);
        LastLowerPressed    = isSwitchPressed(limitLower);

        // Initials state has the arm lowered and the elbow raised.
        armShoulderSet = false;
        armElbowSet    = true;

        ShoulderState = getArmShoulderState();
        ElbowState    = getArmElbowState();
    }

    /*
     * Call this method from the robot's main periodic callback to update the state
     * of the arm. Here we set the ARM motor speed based on the desired value passed in
     * and the states of the two limit switches, stopping the motor if we are commanded
     * to drive past the limit switch in either direction.
     *
     * @param speed - the desired ARM motor speed in the range (-1.0, 1.0).
     *
     * @returns None
     */
    public void periodic()
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

    private void armUp()
    {
        armShoulderSet = true;
        armShoulderSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    private void armDown()
    {
        armShoulderSet = false;
        armShoulderSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

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

    private void armElbowUp()
    {
        armElbowSet = true;
        armElbowSolenoid.set(DoubleSolenoid.Value.kForward);
    }

    private void armElbowDown()
    {
        armElbowSet = false;
        armElbowSolenoid.set(DoubleSolenoid.Value.kReverse);
    }

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
    
    public double getGameTime()
    {
        double gameTime;
        
        gameTime = DriverStation.getInstance().getMatchTime();
        
        return gameTime;
    }
    
    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
