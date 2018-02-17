package org.usfirst.frc6357.robotcode.subsystems.PID;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 *  This class is a PID source for the arm PID
 */
public class EncoderPositionArmPID implements PIDSource
{
    private Encoder myEncoder; // Encoder
    
    /**
     * 
     * @param inEncoder - The encoder
     */
    public EncoderPositionArmPID(Encoder inEncoder)
    {
       myEncoder = inEncoder; 
    }

    /**
     * 
     */
    @Override
    public void setPIDSourceType(PIDSourceType pidSource)
    {
        return;
        
    }
    
    /**
     * 
     */
    @Override
    public PIDSourceType getPIDSourceType()
    {
        return PIDSourceType.kDisplacement;
    }

    /**
     * 
     */
    @Override
    public double pidGet()
    {
        return myEncoder.getDistance();
    }
}
