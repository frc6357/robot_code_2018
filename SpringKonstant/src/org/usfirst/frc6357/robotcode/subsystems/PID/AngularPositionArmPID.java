package org.usfirst.frc6357.robotcode.subsystems.PID;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.SpeedController;

public class AngularPositionArmPID implements PIDOutput 
{
    private final SpeedController mySpeedController;
    private final PIDSource myDistanceMeasurment;
    private final PIDController myPidController;
    
    private final double Kp = 0;
    private final double Ki = 0;
    private final double Kd = 0;
    
    public AngularPositionArmPID(SpeedController inSpeedController, PIDSource inDistanceMeasurment)
    {
        mySpeedController = inSpeedController;
        myDistanceMeasurment = inDistanceMeasurment;
        
        myPidController = new PIDController(Kp, Ki, Kd, myDistanceMeasurment, mySpeedController);
        myPidController.setOutputRange(-1, 1);
    }
    
    public void enable()
    {
        myPidController.enable();
    }
    
    public void disable()
    {
        myPidController.disable();
    }
    
    public void reset()
    {
        myPidController.reset();
        myPidController.enable();
    }
    
    public void setDistanceTarget(double distance)
    {
        myPidController.setSetpoint(distance);
    }
        
    @Override
    public void pidWrite(double output)
    {
        setDistanceTarget(output);
    }
    
    public double getPIDSetpoint()
    {
        return myPidController.getSetpoint();
    }

}
