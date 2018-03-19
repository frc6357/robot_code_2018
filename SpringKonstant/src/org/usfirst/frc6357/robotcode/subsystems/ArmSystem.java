package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import org.usfirst.frc6357.robotcode.Ports;


/**
 * Subsystem controlling the power cube intake mechanism in the robot. This
 * mechanism comprises a single motor connected to a TalonSRX motor controller.
 * Running the motor changes the angle of the arm. We set current limits on the
 * arm to stop the motors if the arm becomes blocked.
 *
 * TODO: If we have an encoder on the arm, we should update this module to use
 * position control. Currently, it merely sets the motor speed and leaves the
 * operator to decide when to start and stop the motor.
 *
 * TODO: Set the current limit parameters sensibly.
 */
public class ArmSystem extends Subsystem
{
    private final DoubleSolenoid armSolenoid;


    public ArmSystem()
    {
        armSolenoid = new DoubleSolenoid (Ports.pcm1, Ports.ArmSolenoidUp, Ports.ArmSolenoidDown);        
    }

    public void armUp()
    {
    	armSolenoid.set(DoubleSolenoid.Value.kForward);
    }
    
    public void armDown()
    {
    	armSolenoid.set(DoubleSolenoid.Value.kReverse);
    }
    
    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
