package org.usfirst.frc6357.robotcode.subsystems;

import org.usfirst.frc6357.robotcode.Ports;
import org.usfirst.frc6357.robotcode.subsystems.IMU.OrientationAxis;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

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

    // These members track the speeds we have actually set the motors to (MotorSpeed)
    // and the speed that the user has commanded us to set (CommandedSpeed).
    private double leftMotorSpeed;
    private double rightMotorSpeed;
    private double leftCommandedSpeed;
    private double rightCommandedSpeed;

    // An instance of the inertial management unit to allow angle measurements. We
    // make this public to allow the robot to access it to make periodic angle readings.
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

        // Configure the IMU.
        driveIMU = new IMU();
        driveIMU.setMajorAxis(OrientationAxis.Z);
        driveIMU.setMovingAverageSamples(20);

        // This sets the all the speed controllers on the right side to follow the
        // center speed controller
        ((WPI_VictorSPX) baseBackRight).set(ControlMode.Follower, ((WPI_VictorSPX) baseFrontRightMaster).getDeviceID());
        ((WPI_VictorSPX) baseCenterRight).set(ControlMode.Follower, ((WPI_VictorSPX) baseFrontRightMaster).getDeviceID());

        // This sets the all the speed controllers on the left side to follow the center
        // speed controller
        ((WPI_VictorSPX) baseCenterLeft).set(ControlMode.Follower, ((WPI_VictorSPX) baseFrontLeftMaster).getDeviceID());
        ((WPI_VictorSPX) baseBackLeft).set(ControlMode.Follower, ((WPI_VictorSPX) baseFrontLeftMaster).getDeviceID());

        leftCommandedSpeed      = 0.0;
        rightCommandedSpeed     = 0.0;
        leftMotorSpeed          = 0.0;
        rightMotorSpeed         = 0.0;
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
        leftCommandedSpeed = speed;
        leftMotorSpeed     = speed;
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
        rightCommandedSpeed = speed;
        rightMotorSpeed     = speed;
        baseFrontRightMaster.set(speed);
    }

    /** 
     * This method returns the currently set speed of the left motor(s).
     */
    public double getLeftSpeed()
    {
        return(leftMotorSpeed);
    }

    /**
    * This method returns the currently set speed of the right motor(s).
    */
    public double getRightSpeed()
    {
        return(rightMotorSpeed);
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
