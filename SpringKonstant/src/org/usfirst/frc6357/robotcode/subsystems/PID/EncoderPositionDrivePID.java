package org.usfirst.frc6357.robotcode.subsystems.PID;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

/**
 * 
 * 
 */
public class EncoderPositionDrivePID implements PIDSource
{
    private final Encoder myEncoder;

    /**
     * 
     * @param inEncoder
     */
    public EncoderPositionDrivePID(Encoder inEncoder)
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
