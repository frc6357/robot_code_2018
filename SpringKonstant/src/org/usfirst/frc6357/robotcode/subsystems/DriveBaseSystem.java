package org.usfirst.frc6357.robotcode.subsystems;

import org.usfirst.frc6357.robotcode.Ports;
import org.usfirst.frc6357.robotcode.subsystems.IMU.OrientationAxis;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;

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
 * TODO: determine all methods the need to be in the Base class for the drive
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

    // Strafing system motors and state
    private final SpeedController baseStrafe;
    private final Solenoid baseStrafeSolenoid;
    private boolean baseStrafeDeployed;

    // An instance of the inertial management unit to allow angle measurements. We make
    // this public to allow the robot to access it to make periodic angle readings.
    public final IMU driveIMU;

    /**
     * The DriveBaseSystem constructor handles all the actuator object creation, and
     * sets the follow mode for the speed controllers
     */
    public DriveBaseSystem()
    {
        super();

        // LEFT DRIVE TALONS
        baseFrontLeftMaster = new WPI_TalonSRX(Ports.DriveLeftFrontMotor);
        baseCenterLeft = new WPI_TalonSRX(Ports.DriveLeftCenterMotor);
        baseBackLeft = new WPI_TalonSRX(Ports.DriveLeftRearMotor);

        // RIGHT DRIVE TALONS
        baseFrontRightMaster = new WPI_TalonSRX(Ports.DriveRightFrontMotor);
        baseCenterRight = new WPI_TalonSRX(Ports.DriveRightCenterMotor);
        baseBackRight = new WPI_TalonSRX(Ports.DriveRightRearMotor);

        // Inverts the speed controllers so they do not spin the wrong way.
        baseBackRight.setInverted(true);
        baseCenterRight.setInverted(true);
        baseFrontRightMaster.setInverted(true);

        // Configure the IMU.
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

        baseStrafeDeployed = false;

        // STRAFE MOTOR CONTROLLER
        baseStrafe = new WPI_VictorSPX(Ports.DriveStrafeMotor);
        baseStrafeSolenoid = new Solenoid(Ports.PCM_ID, Ports.DriveStrafeSolenoid);
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
     * This method is used to deploy or stow the strafing mechanism.
     *
     * @param state
     *            - state us true to deploy the strafing mechanism or false to stow it.
     */
    public void deployStrafe(boolean state)
    {
        baseStrafeSolenoid.set(state);
        baseStrafeDeployed = state;
    }

    /**
     * This method will toggle the deployment state of the strafe mechanism. It it's currently
     * deployed, this call will stow it and vice versa.
     *
     * @param None
     * @return Returns the new state of the strafing mechanism, true if deployed, false if stowed.
     */
    public boolean toggleStrafe()
    {
        deployStrafe(!baseStrafeDeployed);
        return(baseStrafeDeployed);
    }

    /**
     * This method return the current state of the strafing mechanism.
     *
     * @param None
     * @return Returns the current state of the strafing mechanism, true if deployed, false if stowed.
     */
    public boolean getStrafeState()
    {
        return(baseStrafeDeployed);
    }

    /**
     * Each subsystem may, but is not required to, have a default command which is
     * scheduled whenever the subsystem is idle. If default command is needed use
     * "setDefaultCommand(new MyDefaultCommand());"
     */
    @Override
    protected void initDefaultCommand()
    {
        // TODO Auto-generated method stub

    }

}
