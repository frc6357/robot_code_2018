package org.usfirst.frc6357.robotcode.subsystems;

import org.usfirst.frc6357.robotcode.Ports;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *	The DriveBaseSystem subsystem controls all the basic functions of the speed
 *	controllers for the drive train. It includes setting the speed for each side
 *	of the robot, turning an angle, checking various states of the robot including
 *	if the robot is stopped, and other states.
 *
 *	The master speed controllers, "baseFrontLeftMaster" and "baseFrontRightMaster" are followed by the other
 *	speed controllers. Follow means that the speed sent to the master controllers
 *	are sent to the other speed controllers on each side.
 *
 *	TODO: determine all methods the need to be in the Base class for the drive
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
	
	/**
	 *	The DriveBaseSystem constructor handles all the actuator object creation,
	 *	and sets the follow mode for the speed controllers
	 */
	public DriveBaseSystem()
	{
		super();
		
		// LEFT DRIVE TALONS
		baseFrontLeftMaster 	= new WPI_TalonSRX(Ports.DriveLeftFrontMotor);
		baseCenterLeft 			= new WPI_TalonSRX(Ports.DriveLeftCenterMotor);
		baseBackLeft 			= new WPI_TalonSRX(Ports.DriveLeftRearMotor);
	
		// RIGHT DRIVE TALONS
		baseFrontRightMaster	= new WPI_TalonSRX(Ports.DriveLeftFrontMotor);
		baseCenterRight			= new WPI_TalonSRX(Ports.DriveRightCenterMotor);
		baseBackRight 			= new WPI_TalonSRX(Ports.DriveRightRearMotor);
		
		// Inverts the speed controllers so they do not spin the wrong way.
		baseBackRight.setInverted(true);
		baseCenterRight.setInverted(true);
		baseFrontRightMaster.setInverted(true);
		
		// This sets the all the speed controllers on the right side to follow the center speed controller
		((WPI_TalonSRX) baseBackRight).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontRightMaster).getDeviceID());
		((WPI_TalonSRX) baseCenterRight).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontRightMaster).getDeviceID());
		
		// This sets the all the speed controllers on the left side to follow the center speed controller
		((WPI_TalonSRX) baseCenterLeft).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontLeftMaster).getDeviceID());
		((WPI_TalonSRX) baseBackLeft).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontLeftMaster).getDeviceID());
	}
	
	/**
	 * This method is used to send a double to the speed controller on the left side of the robot.
	 * @param speed - speed is the double number between 1 and -1, usually from the joystick axis.
	 */
	public void setLeftSpeed(double speed)
	{
		baseFrontLeftMaster.set(speed);
	}
	
	/**
	 * This method is used to send a double to the speed controller on the right side of the robot.
	 * @param speed - speed is the double number between 1 and -1, usually from the joystick axis.
	 */
	public void setRightSpeed(double speed)
	{
		baseFrontRightMaster.set(speed);
	}
	
	/**
	 * Each subsystem may, but is not required to, have a default command which is scheduled whenever the subsystem is idle.
	 * If default command is needed use "setDefaultCommand(new MyDefaultCommand());"
	 */
	@Override
	protected void initDefaultCommand()
	{
		// TODO Auto-generated method stub
		
	}


}
