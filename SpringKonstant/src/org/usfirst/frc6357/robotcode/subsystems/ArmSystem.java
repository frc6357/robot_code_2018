package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc6357.robotcode.Ports;
import org.usfirst.frc6357.robotcode.subsystems.PID.AngularPositionArmPID;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

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
    private final SpeedController armMotor;
    private final DigitalInput limitUpper;
    private final DigitalInput limitLower;
    private boolean tripLower = false;
    private boolean tripUpper = false;

    public ArmSystem()
    {
        
        armMotor = new WPI_TalonSRX(Ports.ArmElevationMotor);
        armMotor.set(0.0);
        
        /*
        ((WPI_TalonSRX) armMotor).configPeakCurrentLimit(35, 10);       // 35 A
        ((WPI_TalonSRX) armMotor).configPeakCurrentDuration(200, 10);   // 200ms
        ((WPI_TalonSRX) armMotor).configContinuousCurrentLimit(30, 10); // 30A
        ((WPI_TalonSRX) armMotor).enableCurrentLimit(true);*/

        //armEncoder = new Encoder(Ports.ArmEncoderA, Ports.ArmEncoderB);
        
        limitUpper = new DigitalInput(Ports.ArmLimitTop);
        limitLower = new DigitalInput(Ports.ArmLimitBottom);
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
    public void periodic(double speed)
    {
        double calcSpeed = speed;
        boolean upperState, lowerState;

        // Check the state of the limit switches and stop the motor if necessary.
        // The switches are normally open and the DIO pins on RoboRio are pulled up
        // so, if we read 1, this means the switch isn't pressed while 0 means that
        // the switch is pressed.
        upperState = limitUpper.get();
        lowerState = limitLower.get();

        //TODO check the polarity of the motor speed, joystick
        // if tripped state is true == do not move the motor upwards
        if(upperState == true)
        {
            tripUpper = true;

            if(calcSpeed > 0.0)
            {
                calcSpeed = 0.0;
            }
        }
        else
        {
            tripUpper = false;
        }
        
        //if tripped state is true == do not move the motor down
        if(lowerState == true)
        {
            tripLower = true;

            if(calcSpeed < 0.0)
            {
                calcSpeed = 0.0;
            }
        }
        else
        {
            tripLower = false;
        }

        setArmSpeed(calcSpeed);

        SmartDashboard.putNumber("ARM current", ((BaseMotorController) armMotor).getOutputCurrent());
        SmartDashboard.putBoolean("ARM Upper Limit", tripUpper);
        SmartDashboard.putBoolean("ARM Lower Limit", tripLower);
    }
    
    private void setArmSpeed(double speed)
    {
        SmartDashboard.putNumber("ARM speed", speed);
        
        armMotor.set(speed / 2);
    }

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
