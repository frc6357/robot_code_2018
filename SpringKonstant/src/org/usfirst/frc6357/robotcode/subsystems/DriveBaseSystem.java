package org.usfirst.frc6357.robotcode.subsystems;

import org.usfirst.frc6357.robotcode.Ports;
import org.usfirst.frc6357.robotcode.subsystems.IMU.OrientationAxis;
import org.usfirst.frc6357.robotcode.subsystems.PID.PositionAndVelocityControlledDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * The DriveBaseSystem subsystem controls all the basic functions of the speed
 * controllers for the drive train. It includes setting the speed for each side
 * of the robot, turning an angle, checking various states of the robot
 * including if the robot is stopped, and other states.
 *
 * The master speed controllers, "baseFrontLeftMaster" and
 * "baseFrontRightMaster" are followed by the other speed controllers. Follow
 * means that the speed sent to the master controllers are sent to the other
 * speed controllers on each side.
 *
 * TODO: determine all methods that need to be in the Base class for the drive
 */
public class DriveBaseSystem extends Subsystem
{
    // Speed Controllers for the drive
    // The master speed controller is followed by the other speed controllers
    private final SpeedController baseFrontLeftMaster; // Master speed controller
    private final SpeedController baseCenterLeft;
    private final SpeedController baseBackLeft;
    private final SpeedController baseFrontRightMaster; // Master speed controller
    private final SpeedController baseCenterRight;
    private final SpeedController baseBackRight;

    // Gear shifter
    private final DoubleSolenoid baseGearShiftSolenoid;
    private boolean baseHighGear;

    // Encoders
    private final Encoder rightEncoder;
    private final Encoder leftEncoder;

    // Strafing system motors and state
    private final SpeedController baseStrafe;
    private final DoubleSolenoid baseStrafeSolenoid;
    private final Solenoid baseFrontLiftSolenoid;
    private final Solenoid baseBackLiftSolenoid;
    private boolean baseStrafeDeployed;
    private final StrafingAngleController baseStrafeAngleController;

    // PID for drive
    private final PositionAndVelocityControlledDrive leftSide;
    private final PositionAndVelocityControlledDrive rightSide;

    private boolean isInVelocityMode;

    // An instance of the inertial management unit to allow angle measurements. We
    // make
    // this public to allow the robot to access it to make periodic angle readings.
    public final IMU driveIMU;

    /**
     * The DriveBaseSystem constructor handles all the actuator object creation, and
     * sets the follow mode for the speed controllers
     */
    public DriveBaseSystem()
    {
        super();

        // TODO: Change these to VictorSPX controllers when we move to the
        // new robot chassis. We left them as Talons for now while testing
        // on the 2017 robot.

        // Left Drive Controllers
        baseFrontLeftMaster = new WPI_TalonSRX(Ports.DriveLeftFrontMotor);
        baseCenterLeft = new WPI_TalonSRX(Ports.DriveLeftCenterMotor);
        baseBackLeft = new WPI_TalonSRX(Ports.DriveLeftRearMotor);

        // Right Drive Controllers
        baseFrontRightMaster = new WPI_TalonSRX(Ports.DriveRightFrontMotor);
        baseCenterRight = new WPI_TalonSRX(Ports.DriveRightCenterMotor);
        baseBackRight = new WPI_TalonSRX(Ports.DriveRightRearMotor);

        // Inverts the speed controllers so they do not spin the wrong way.
        baseBackRight.setInverted(true);
        baseCenterRight.setInverted(true);
        baseFrontRightMaster.setInverted(true);

        // Encoders
        leftEncoder = new Encoder(Ports.DriveLeftEncoderA, Ports.DriveLeftEncoderB);
        rightEncoder = new Encoder(Ports.DriveRightEncoderA, Ports.DriveRightEncoderB);

        // Configure the IMU.
        // TODO: Set the appropriate axis here depending upon the final
        // orientation of the IMU in the robot.
        driveIMU = new IMU();
        driveIMU.setMajorAxis(OrientationAxis.X);
        driveIMU.setMovingAverageSamples(20);

        // This sets the all the speed controllers on the right side to follow the
        // center speed controller
        ((WPI_TalonSRX) baseBackRight).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontRightMaster).getDeviceID());
        ((WPI_TalonSRX) baseCenterRight).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontRightMaster).getDeviceID());

        // This sets the all the speed controllers on the left side to follow the center
        // speed controller
        ((WPI_TalonSRX) baseCenterLeft).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontLeftMaster).getDeviceID());
        ((WPI_TalonSRX) baseBackLeft).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontLeftMaster).getDeviceID());

        // Strafing motor
        baseStrafe = new WPI_VictorSPX(Ports.DriveStrafeMotor);

        // Strafing angle controller
        baseStrafeAngleController = new StrafingAngleController(driveIMU);

        // Lift system
        baseStrafeSolenoid = new DoubleSolenoid(Ports.PCM_ID, Ports.DriveStrafeSolenoidUp,
                Ports.DriveStrafeSolenoidDown);
        baseFrontLiftSolenoid = new Solenoid(Ports.PCM_ID, Ports.DriveLiftSolenoidFront);
        baseBackLiftSolenoid = new Solenoid(Ports.PCM_ID, Ports.DriveLiftSolenoidBack);

        baseStrafeDeployed = false;

        // Gear shifter
        baseGearShiftSolenoid = new DoubleSolenoid(Ports.PCM_ID, Ports.DriveGearSolenoidLow,
                Ports.DriveGearSolenoidHigh);
        baseHighGear = true;

        // PID
        leftSide = new PositionAndVelocityControlledDrive(baseFrontRightMaster, rightEncoder);
        rightSide = new PositionAndVelocityControlledDrive(baseFrontLeftMaster, leftEncoder);

        isInVelocityMode = false;

        // Set initial states of all actuators
        leftEncoder.reset();
        rightEncoder.reset();
        deployStrafe(baseStrafeDeployed);
        setHighGear(baseHighGear);
    }

    /**
     * This method is used to send a double to the speed controller on the left side
     * of the robot.
     *
     * @param speed
     *            - speed is the double number between 1 and -1, usually from the
     *            joystick axis.
     */
    public void setLeftSpeed(double speed)
    {
        baseFrontLeftMaster.set(speed);
    }

    /**
     * This method is used to send a double to the speed controller on the right
     * side of the robot.
     *
     * @param speed
     *            - speed is the double number between 1 and -1, usually from the
     *            joystick axis.
     */
    public void setRightSpeed(double speed)
    {
        baseFrontRightMaster.set(speed);
    }

    /**
     * Enables the PID
     */
    public void enable()
    {
        leftSide.enable();
        rightSide.enable();
    }

    /**
     * Disables the PID
     */
    public void disable()
    {
        leftSide.disable();
        rightSide.disable();
    }

    /**
     * Sets the mode to postition mode
     */
    public void setPositionMode()
    {
        leftSide.setPositionMode();
        rightSide.setPositionMode();

        leftEncoder.reset();
        rightEncoder.reset();

        isInVelocityMode = false;
    }

    /**
     * Sets the mode to velocity mode
     */
    public void setVelocityMode()
    {
        leftSide.setVelocityMode();
        rightSide.setVelocityMode();
        leftEncoder.reset();
        rightEncoder.reset();

        isInVelocityMode = true;
    }

    /**
     * @return returns true if in velocity
     */
    public boolean isInVelocityMode()
    {
        return isInVelocityMode;
    }

    /**
     * Sets the target velocity for the PID
     * 
     * @param speed
     *            - Speed in feet per second
     */
    public void setLeftTargetVelocity(double speed)
    {
        leftSide.setSpeedAbsolute(speed);
    }

    /**
     * Sets the target velocity for the PID
     * 
     * @param speed
     *            - Speed in feet per second
     */
    public void setRightTargetVelocity(double speed)
    {
        rightSide.setSpeedAbsolute(speed);
    }

    /**
     * Sets the PID set point, which drives the robot straight
     */
    public void driveStraight(double distance)
    {
        leftSide.setDistanceTarget(distance);
    }

    /**
     * This method is used to set the speed of the strafing motor.
     *
     * @param speed
     *            - speed is the double number between 1 and -1, usually from the
     *            joystick axis.
     */
    public void setStrafeSpeed(double speed)
    {
        baseStrafe.set(speed);
    }

    /**
     * Get the drive motor speed adjustment to set to counteract unwanted rotation
     * while strafing.
     *
     * @return Returns an absolute speed differential to set between left and right
     *         drive motors. Apply half this adjustment to the left motor speed and
     *         negative half to the right motor speed.
     */
    public double getStrafeRotateAdjust()
    {
        if (baseStrafeDeployed)
        {
            return baseStrafeAngleController.getSpeedAdjust();
        } else
        {
            return 0.0;
        }
    }

    /**
     * This method is used to deploy or stow the strafing mechanism.
     *
     * @param state
     *            - state us true to deploy the strafing mechanism or false to stow
     *            it.
     */
    public void deployStrafe(boolean state)
    {
        double angle;

        if (state)
        {
            baseStrafeSolenoid.set(DoubleSolenoid.Value.kForward);
            baseFrontLiftSolenoid.set(true);
            baseBackLiftSolenoid.set(true);

            angle = baseStrafeAngleController.getCurrentAngle();
            baseStrafeAngleController.setAngleSetpoint(angle);
            baseStrafeAngleController.enable();
        } else
        {
            baseStrafeSolenoid.set(DoubleSolenoid.Value.kReverse);
            baseFrontLiftSolenoid.set(false);
            baseBackLiftSolenoid.set(false);

            baseStrafeAngleController.disable();
        }

        baseStrafeDeployed = state;
    }

    /**
     * This method will toggle the deployment state of the strafe mechanism. It it's
     * currently deployed, this call will stow it and vice versa.
     *
     * @param None
     * @return Returns the new state of the strafing mechanism, true if deployed,
     *         false if stowed.
     */
    public boolean toggleStrafe()
    {
        deployStrafe(!baseStrafeDeployed);
        return (baseStrafeDeployed);
    }

    /**
     * This method return the current state of the strafing mechanism.
     *
     * @param None
     * @return Returns the current state of the strafing mechanism, true if
     *         deployed, false if stowed.
     */

    public boolean getStrafeState()
    {
        return (baseStrafeDeployed);
    }

    /**
     * This method is used to change between low and high gear ratios.
     *
     * @param state
     *            - state is true to switch to high gear, false to switch to low
     *            gear.
     */
    public void setHighGear(boolean high)
    {
        if (high)
        {
            baseGearShiftSolenoid.set(DoubleSolenoid.Value.kReverse);
        } else
        {
            baseGearShiftSolenoid.set(DoubleSolenoid.Value.kForward);
        }
        baseHighGear = high;
    }

    public double getLeftEncoderRaw() // Returns raw value of the encoder
    {
        return leftEncoder.getRaw();
    }

    public double getRightEncoderRaw() // Returns raw value of the encoder
    {
        return rightEncoder.getRaw();
    }

    public double getLeftEncoderRate() // Returns the current rate of the encoder
    {
        return leftEncoder.getRate();
    }

    public double getRightEncoderRate() // Returns the current rate of the encoder
    {
        return rightEncoder.getRate();
    }

    /**
     * Each subsystem may, but is not required to, have a default command which is
     * scheduled whenever the subsystem is idle. If default command is needed use
     * "setDefaultCommand(new MyDefaultCommand());"
     */
    @Override
    protected void initDefaultCommand()
    {
      
    }

}
