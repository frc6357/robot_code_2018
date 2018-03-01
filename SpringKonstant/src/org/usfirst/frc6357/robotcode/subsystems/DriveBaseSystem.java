package org.usfirst.frc6357.robotcode.subsystems;

import org.usfirst.frc6357.robotcode.Ports;
import org.usfirst.frc6357.robotcode.subsystems.IMU.OrientationAxis;
import org.usfirst.frc6357.robotcode.subsystems.PID.PositionAndVelocityControlledDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
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
    private final double DISTANCE_PER_PULSE;
    private final int PULSES_PER_ROTATION;
    private final int DRIVE_WHEEL_RADIUS;

    // Strafing system motors and state
    private final SpeedController baseStrafe;
    private final Solenoid baseStrafeSolenoid;
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

        // NB: We are using 3 motors through a gearbox on each side of the robot. In
        // this arrangement, the front and rear motors must run in one direction
        // and the matchign center motor must run in the opposite direction. Take
        // care to ensure that this is correct!

        // Left Drive Controllers
        baseFrontLeftMaster = new WPI_VictorSPX(Ports.driveLeftFrontMotor);
        baseCenterLeft = new WPI_VictorSPX(Ports.driveLeftCenterMotor);
        baseBackLeft = new WPI_VictorSPX(Ports.driveLeftRearMotor);

        // Right Drive Controllers
        baseFrontRightMaster = new WPI_VictorSPX(Ports.driveRightFrontMotor);
        baseCenterRight = new WPI_VictorSPX(Ports.driveRightCenterMotor);
        baseBackRight = new WPI_VictorSPX(Ports.driveRightRearMotor);

        // Inverts the speed controllers so they do not spin the wrong way.
        baseBackRight.setInverted(true);
        baseCenterRight.setInverted(false);
        baseFrontRightMaster.setInverted(true);

        baseBackLeft.setInverted(false);
        baseCenterLeft.setInverted(true);
        baseFrontLeftMaster.setInverted(false);

        // Encoders
        leftEncoder = new Encoder(Ports.DriveLeftEncoderA, Ports.DriveLeftEncoderB);
        rightEncoder = new Encoder(Ports.DriveRightEncoderA, Ports.DriveRightEncoderB);

        // Configure the IMU.
        driveIMU = new IMU();
        driveIMU.setMajorAxis(OrientationAxis.Z);
        driveIMU.setMovingAverageSamples(20);

        // This sets the all the speed controllers on the right side to follow the
        // center speed controller
        ((WPI_VictorSPX) baseBackRight).set(ControlMode.Follower, ((WPI_VictorSPX) baseFrontRightMaster).getDeviceID());
        ((WPI_VictorSPX) baseCenterRight).set(ControlMode.Follower,
                ((WPI_VictorSPX) baseFrontRightMaster).getDeviceID());

        // This sets the all the speed controllers on the left side to follow the center
        // speed controller
        ((WPI_VictorSPX) baseCenterLeft).set(ControlMode.Follower, ((WPI_VictorSPX) baseFrontLeftMaster).getDeviceID());
        ((WPI_VictorSPX) baseBackLeft).set(ControlMode.Follower, ((WPI_VictorSPX) baseFrontLeftMaster).getDeviceID());

        // Strafing motor
        baseStrafe = new WPI_VictorSPX(Ports.driveStrafeMotor);

        // Strafing angle controller
        baseStrafeAngleController = new StrafingAngleController(driveIMU);

        // Lift system
        baseStrafeSolenoid = new Solenoid(Ports.pcm1, Ports.hDriveSolenoid);
        baseFrontLiftSolenoid = new Solenoid(Ports.pcm1, Ports.frontButterflyDown);
        baseBackLiftSolenoid = new Solenoid(Ports.pcm1, Ports.backButterflyDown);

        baseStrafeDeployed = false;

        // Gear shifter
        baseGearShiftSolenoid = new DoubleSolenoid(Ports.pcm1, Ports.driveGearShiftHigh, Ports.driveGearShiftLow);
        baseHighGear = true;

        // PID
        leftSide = new PositionAndVelocityControlledDrive(baseFrontRightMaster, rightEncoder);
        rightSide = new PositionAndVelocityControlledDrive(baseFrontLeftMaster, leftEncoder);

        isInVelocityMode = false;

        // Set initial states of all actuators
        PULSES_PER_ROTATION = 1000;
        DRIVE_WHEEL_RADIUS = 2; // This is in inches
        DISTANCE_PER_PULSE = 2 * Math.PI * DRIVE_WHEEL_RADIUS / PULSES_PER_ROTATION;

        setEncoderDistancePerPulse();
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
     * 
     * @param distance
     *            the distance to drive forwards
     */
    public void driveStraight(double distance)
    {
        leftSide.setDistanceTarget(distance);
        rightSide.setDistanceTarget(distance);
    }

    /**
     * Turns the robot around, with degrees being positive for clockwise
     * 
     * @param degrees
     *            angle at which to rotate with positive being clockwise
     */
    public void turnDegrees(double degrees)
    {
        leftSide.setDistanceTarget(getTurnDistance(degrees));
        leftSide.setDistanceTarget(-1 * getTurnDistance(degrees));
    }

    public double getTurnDistance(double angle) // Turns angle to the distance
    {
        return (2 * Math.PI * 11.125 / (12)) * (angle / 360.0); // TODO finalize math
    }

    /**
     * Sets the distance per encoder pulse TODO set this distance
     * 
     * @param distance
     *            in any unit
     */
    public void setEncoderDistancePerPulse()
    {
        leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
    }

    /**
     * This method is used to set the speed of the strafing motor.
     *
     * @param speed
     *            - speed is the double number between 1 and -1, usually from the
     *            joystick axis.
     */
    public void setStrafeSpeed(double rightAxis, double leftAxis)
    {
        double speed = leftAxis - rightAxis;

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
     *            - state is true to deploy the strafing mechanism or false to stow
     *            it.
     */
    public void deployStrafe(boolean state)
    {
        double angle;

        if (state)
        {
            baseStrafeSolenoid.set(true);
            baseFrontLiftSolenoid.set(true);
            baseBackLiftSolenoid.set(true);

            angle = baseStrafeAngleController.getCurrentAngle();
            baseStrafeAngleController.setAngleSetpoint(angle);
            baseStrafeAngleController.enable();
        } else
        {
            baseStrafeSolenoid.set(false);
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
