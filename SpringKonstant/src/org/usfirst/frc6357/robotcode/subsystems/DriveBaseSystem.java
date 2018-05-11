package org.usfirst.frc6357.robotcode.subsystems;

import org.usfirst.frc6357.robotcode.Ports;
import org.usfirst.frc6357.robotcode.Robot;
import org.usfirst.frc6357.robotcode.subsystems.ArmSystem.ArmState;
import org.usfirst.frc6357.robotcode.subsystems.IMU.OrientationAxis;

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
    private boolean slowMode;

    // Encoders
    public final Encoder rightEncoder;
    public final Encoder leftEncoder;
    private final int PULSES_PER_ROTATION = 256;
    private final int DRIVE_WHEEL_RADIUS = 2;
    private final double DISTANCE_PER_PULSE = 2 * DRIVE_WHEEL_RADIUS * Math.PI / PULSES_PER_ROTATION;

    // Strafing system motors and state
    private final Solenoid baseFrontLiftSolenoid;
    private final Solenoid baseBackLiftSolenoid;
    private boolean baseStrafeDeployed;

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
        ((WPI_VictorSPX) baseCenterRight).set(ControlMode.Follower, ((WPI_VictorSPX) baseFrontRightMaster).getDeviceID());

        // This sets the all the speed controllers on the left side to follow the center
        // speed controller
        ((WPI_VictorSPX) baseCenterLeft).set(ControlMode.Follower, ((WPI_VictorSPX) baseFrontLeftMaster).getDeviceID());
        ((WPI_VictorSPX) baseBackLeft).set(ControlMode.Follower, ((WPI_VictorSPX) baseFrontLeftMaster).getDeviceID());

        // Lift system
        //baseStrafeSolenoid = new Solenoid(Ports.drivePCM, Ports.hDriveSolenoid);
        baseFrontLiftSolenoid = new Solenoid(Ports.drivePCM, Ports.frontButterflyDown);
        baseBackLiftSolenoid = new Solenoid(Ports.drivePCM, Ports.backButterflyDown);

        baseStrafeDeployed = false;

        // Gear shifter
        baseGearShiftSolenoid = new DoubleSolenoid(Ports.drivePCM, Ports.driveGearShiftHigh, Ports.driveGearShiftLow);
        baseHighGear = false;

        // Slow Mode
        slowMode = false;
        
        // Sets Defaults
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
        if(Robot.armSystem.getArmShoulderState() == ArmState.UP)
            baseFrontLeftMaster.set(speed / 10);
        else if(slowMode)
            baseFrontLeftMaster.set(speed / 2);
        else
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
        if(Robot.armSystem.getArmShoulderState() == ArmState.UP)
            baseFrontRightMaster.set(speed / 10);
        else if(slowMode)
            baseFrontRightMaster.set(speed / 2);
        else
            baseFrontRightMaster.set(speed);
    }

    /**
     * Turns the robot around, with degrees being positive for clockwise
     *
     * @param degrees
     *            angle at which to rotate with positive being clockwise
     */
    public void turnDegrees(double angle)
    {
        driveIMU.reset();
        double turnSpeed = 0;
        double currentAngle = driveIMU.updatePeriodic() + 180;
        double targetAngle = currentAngle + angle;
        if(targetAngle < 0) targetAngle += 360;
        if(targetAngle > 360) targetAngle -= 360;

        for(int i=0; i<60; i++)
        {
        	turnSpeed = Math.min(Math.abs(targetAngle - currentAngle)/25, 1.0);
            if(angle > 0)    //If turning right
            {
                setRightSpeed(-.5*turnSpeed);
                setLeftSpeed(.5*turnSpeed);
            }
            else
            {
                setRightSpeed(.5*turnSpeed);
                setLeftSpeed(-.5*turnSpeed);
            }
            currentAngle = driveIMU.updatePeriodic() + 180;
            if(Math.abs(targetAngle - currentAngle) < 1.0) break;
            try{Thread.sleep(20);}
            catch(Exception e) {}
        }
        setRightSpeed(0);
        setLeftSpeed(0);
        rightEncoder.reset();
        leftEncoder.reset();
    }
    
    public void straightDrive(double speed){
    	double angle = driveIMU.updatePeriodic();
        Robot.driveBaseSystem.setLeftSpeed(speed - 0.007 * angle);
        Robot.driveBaseSystem.setRightSpeed(speed + 0.007 * angle);
    	
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
     * This method is used to deploy or stow the strafing mechanism.
     *
     * @param state
     *            - state is true to deploy the strafing mechanism or false to stow
     *            it.
     */
    public void deployStrafe(boolean state)
    {
        if (state)
        {
            baseFrontLiftSolenoid.set(true);
            baseBackLiftSolenoid.set(true);

        } 
        else
        {
            baseFrontLiftSolenoid.set(false);
            baseBackLiftSolenoid.set(false);
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
     * High gear is push, low gear is pull
     *
     * @param state
     *            - state is true to switch to high gear, false to switch to low
     *            gear.
     */
    public void setHighGear(boolean high)
    {
        if (high)
        {
            baseGearShiftSolenoid.set(DoubleSolenoid.Value.kForward);
        } else
        {
            baseGearShiftSolenoid.set(DoubleSolenoid.Value.kReverse);
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
    
    public void setSlowMode(boolean mode)
    {
        slowMode = mode;
    }
    
    public boolean getSlowMode()
    {
        return slowMode;
    }
    
    public void driveTimeDistance(double inches)
    {
    	double driveSpeed = 0.1;
    	int i = 0;
    	int accelSteps = 10;
    	int accelStepTime = 140; // in milliseconds
    	long totalDriveTime = (long)(Math.abs(inches*1000)/Ports.INCHESPERSECOND);
    	driveIMU.reset();
        straightDrive(inches > 0 ? driveSpeed : -1.0*driveSpeed);
        try {
        	for(i = 0; i < accelSteps; i++){
        		Thread.sleep(accelStepTime/4);
        		driveSpeed = (i+1) * 0.1;
        		straightDrive(inches > 0 ? driveSpeed : -1.0*driveSpeed);
        		Thread.sleep(accelStepTime/4);
        		straightDrive(inches > 0 ? driveSpeed : -1.0*driveSpeed);
        		Thread.sleep(accelStepTime/4);
        		straightDrive(inches > 0 ? driveSpeed : -1.0*driveSpeed);
        		Thread.sleep(accelStepTime/4);
        		straightDrive(inches > 0 ? driveSpeed : -1.0*driveSpeed);
        	}
        	driveSpeed = 1.0;
            Robot.driveBaseSystem.setLeftSpeed(inches > 0 ? driveSpeed : -1.0*driveSpeed);
            Robot.driveBaseSystem.setRightSpeed(inches > 0 ? driveSpeed : -1.0*driveSpeed);
        	Thread.sleep(totalDriveTime - accelSteps*accelStepTime);
        }
        catch(Exception e) {}
        Robot.driveBaseSystem.setLeftSpeed(0);
        Robot.driveBaseSystem.setRightSpeed(0);
        try {Thread.sleep(3000);}
        catch(Exception e) {}
    }
    
    public boolean isStrafeDeployed()
    {
        return baseStrafeDeployed;
    }
}
