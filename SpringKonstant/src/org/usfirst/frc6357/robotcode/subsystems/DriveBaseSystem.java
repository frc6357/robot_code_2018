package org.usfirst.frc6357.robotcode.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveBaseSystem extends Subsystem
{
	// Speed Controllers for the drive
	private final SpeedController baseFrontLeft;
	private final SpeedController baseCenterLeft;
	private final SpeedController baseBackLeft;
	private final SpeedController baseFrontRight;
	private final SpeedController baseCenterRight;
	private final SpeedController baseBackRight;
	
	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	public DriveBaseSystem()
	{
		super();
		
		// LEFT DRIVE TALONS
		baseFrontLeft = new WPI_TalonSRX(10);
		baseCenterLeft = new WPI_TalonSRX(11);
		((WPI_TalonSRX) baseCenterLeft).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontLeft).getDeviceID());
		baseBackLeft = new WPI_TalonSRX(15);
		((WPI_TalonSRX) baseBackLeft).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontLeft).getDeviceID());
	
		// RIGHT DRIVE TALONS
		baseFrontRight = new WPI_TalonSRX(12);
		baseFrontRight.setInverted(true);
		baseCenterRight = new WPI_TalonSRX(14);
		baseCenterRight.setInverted(true);
		baseBackRight = new WPI_TalonSRX(16);
		baseBackRight.setInverted(true);
		
		// This sets the SpeedControllers
		((WPI_TalonSRX) baseBackRight).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontRight).getDeviceID());
		((WPI_TalonSRX) baseCenterRight).set(ControlMode.Follower, ((WPI_TalonSRX) baseFrontRight).getDeviceID());
	}
	
	public void setLeftSpeed(double speed)
	{
		
	}

	@Override
	protected void initDefaultCommand()
	{
		// TODO Auto-generated method stub
		
	}
}
