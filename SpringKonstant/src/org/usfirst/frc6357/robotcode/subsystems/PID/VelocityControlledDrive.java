package org.usfirst.frc6357.robotcode.subsystems.PID;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class VelocityControlledDrive extends Subsystem
{
    
    //private final SpeedController mySpeedController;
    //private final PIDSource mySpeedMeasurement;
    //private final PIDController myPidController;

    private final double Kp = 0.1;
    private final double Kd = 0.0;
    private final double Ki = 0.0;

    private final double maxRobotSpeed = 9.5; // TODO set the feet per second
    
   

    public void initDefaultCommand()
    {

    }
}
