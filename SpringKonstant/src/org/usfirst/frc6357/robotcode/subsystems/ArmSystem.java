package org.usfirst.frc6357.robotcode.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc6357.robotcode.Ports;

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
    private final WPI_TalonSRX armMotor;
    private final DigitalInput limitUpper;
    private final DigitalInput limitLower;
    private boolean tripLower = false;
    private boolean tripUpper = false;

    public ArmSystem()
    {
        armMotor = new WPI_TalonSRX(Ports.ArmElevationMotor);
        /*
<<<<<<< HEAD
         * armMotor.set(0.0); armMotor.configPeakCurrentLimit(35, 10); // 35 A
         * armMotor.configPeakCurrentDuration(200, 10); // 200ms
         * armMotor.configContinuousCurrentLimit(30, 10); // 30A
         * armMotor.enableCurrentLimit(true);
         */
=======
        armMotor.set(0.0);
        armMotor.configPeakCurrentLimit(35, 10);       // 35 A
        armMotor.configPeakCurrentDuration(200, 10);   // 200ms
        armMotor.configContinuousCurrentLimit(30, 10); // 30A
        armMotor.enableCurrentLimit(true);
        */

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
    public void Periodic(double speed)
    {
        double calcSpeed = speed;
        boolean upperState, lowerState;

        // Check the state of the limit switches and stop the motor if necessary.
        // The switches are normally open and the DIO pins on RoboRio are pulled up
        // so, if we read 1, this means the switch isn't pressed while 0 means that
        // the switch is pressed.
        upperState = limitUpper.get();
        lowerState = limitLower.get();

        if(upperState == false)
        {
            tripUpper = true;

            if(calcSpeed > 0.0)
            {
                calcSpeed = 0.0;
            }
        }
        else
        {
            tripLower = false;
        }

        if(lowerState == false)
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

        SmartDashboard.putNumber("ARM speed", calcSpeed);
        SmartDashboard.putNumber("ARM current", armMotor.getOutputCurrent());
        SmartDashboard.putBoolean("ARM Upper Limit", tripUpper);
        SmartDashboard.putBoolean("ARM Lower Limit", tripLower);
>>>>>>> fb97cc594c408298ae19283321e6099499704689
    }

    private void setArmSpeed(double speed)
    {
        armMotor.set(speed);
    }

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
