package org.usfirst.frc6357.robotcode.subsystems;

import org.usfirst.frc6357.robotcode.Ports;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Subsystem controlling the power cube intake mechanism in the robot. This mechanism comprises two motors connected to TalonSRX motor
 * controllers and a pneumatic actuator to swing the mechanism upwards into its stowed position. Current limits are set on the motors to
 * cause them to stop automatically when blocked.
 */
public class IntakeSystem extends Subsystem
{
    public final WPI_TalonSRX intakeLeftMotor;
    public final WPI_TalonSRX intakeRightMotor;
    private final DoubleSolenoid grippers;
    public boolean intakeIsUp = true;
    public boolean gripperOpen = true;

    public IntakeSystem()
    {
        //Speed Controllers for Intake
        intakeLeftMotor = new WPI_TalonSRX(Ports.IntakeLeftMotor);
        intakeRightMotor = new WPI_TalonSRX(Ports.IntakeRightMotor);

        //Pnumantics for the intake grippers
        grippers = new DoubleSolenoid(Ports.pcm2, Ports.OIOperatorIntakeIn, Ports.OIOperatorIntakeOut);

    }

    /**
     * Sets the raw speed of the intake motors.
     *
     * @param speed
     *            The speed setting for the intake motors. Valid values are in the range -1.0 to 1.0.
     *
     * @return None
     */
    public void setIntakeSpeed(double leftAxis, double rightAxis)
    {
        double speed = leftAxis - rightAxis;

        intakeLeftMotor.set(speed);
        intakeRightMotor.set(speed);

        SmartDashboard.putNumber("Intake speed", speed);
        SmartDashboard.putNumber("Intake left current", intakeLeftMotor.getOutputCurrent());
        SmartDashboard.putNumber("Intake right current", intakeRightMotor.getOutputCurrent());
    }

    /**
     * Sets the state of the grippers on the intake based on a boolean
     * 
     * @param state - True == Open Grippers : False == Close Grippers
     */
    public void setIntakeGrippers(boolean state) 
    { 
        if(state) 
        { 
            openIntakeGripper(); 
        }
        else 
        { 
            closeIntakeGripper(); 
        } 
    }

    /**
     * Opens the gripper
     */
     public void openIntakeGripper() 
     { 
         grippers.set(DoubleSolenoid.Value.kReverse); 
         gripperOpen = true; 
     }
      
     
    /** Closes the gripper
     * 
     */
     public void closeIntakeGripper() 
     { 
         grippers.set(DoubleSolenoid.Value.kForward); 
         gripperOpen = false; 
     }
     

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
