
package org.usfirst.frc6357.robotcode.subsystems;

/**
 * This is a basic subclass of the ADIS16448_IMU Inertial Management Unit device
 * which allows us to read and smooth one of the angles reported using a simple
 * moving average.
 *
 * New functions are
 */
public class IMU extends ADIS16448_IMU
{
    public enum OrientationAxis
    {
        X, Y, Z
    };

    public double AngleAverage = 0.0;

    private boolean FirstCall = true;
    private int NumAvgSamples = 10;
    private OrientationAxis MajorAxis = OrientationAxis.X;

    /**
     * Constructor for the IMU class allowing caller to specify both the yaw axis
     * and algorithm.
     *
     * @param yaw_axis
     *            Which axis is Yaw
     * @param algorithm
     *            Use AHRSAlgorithm.kComplementary or AHRSAlgorithm.kMadgwick
     */
    public IMU(Axis yaw_axis, AHRSAlgorithm algorithm)
    {
        super(yaw_axis, algorithm);
    }

    /**
     * Constructor for the IMU class. This variant assumes the kComplementary
     * algorithm.
     *
     * @param yaw_axis
     *            Which axis is Yaw
     */
    public IMU(Axis yaw_axis)
    {
        this(yaw_axis, AHRSAlgorithm.kComplementary);
    }

    /*
     * Basic constructor for the IMU class. This variant assumes yaw axis is "Z" and
     * uses the kComplementary algorithm.
     */
    public IMU()
    {
        this(Axis.kZ, AHRSAlgorithm.kComplementary);
    }

    /**
     * This function must be called periodically to read the angle reported for the
     * major axis and update the moving average.
     *
     * @return Returns the current moving average for the angle of the major axis.
     */
    public double updatePeriodic()
    {
        double Angle = getRawAngle();

        if (FirstCall)
        {
            AngleAverage = Angle;
        }

        AngleAverage -= AngleAverage / NumAvgSamples;
        AngleAverage += Angle / NumAvgSamples;

        return (AngleAverage);
    }

    /**
     * Queries the current moving average calculated for the major axis.
     *
     * @return Returns the current moving average for the angle of the major axis.
     */
    public double getAngle()
    {
        return (AngleAverage);
    }

    /**
     * Queries the current, unaveraged angle measurement from the major axis.
     *
     * @return Returns the current raw angle reeorted for the major axis.
     */
    public double getRawAngle()
    {
        double Angle = 0.0;

        switch (MajorAxis)
        {
            case X:
                Angle = getAngleX();
                break;
            case Y:
                Angle = getAngleY();
                break;
            case Z:
                Angle = getAngleZ();
                break;
        }

        return(Angle);
    }

    /**
     * This function must be called periodically to read the angle reported for the
     * major axis and update the moving average.
     *
     * @param axis
     *            is the identifier of the major axis whose angle reading will be
     *            averaged. Valid values are OrientationAxis.X, OrientationAxis.Y
     *            and OrientationAxis.Z.
     *
     * @return None
     */
    public void setMajorAxis(OrientationAxis axis)
    {
        MajorAxis = axis;
        FirstCall = true;
    }

    /**
     * Sets the number of samples over which the angle moving average is calculated.
     * The default is 10.
     *
     * @param count
     *            is the number of samples used in the moving average calculation.
     *
     * @return None
     */
    public void setMovingAverageSamples(int count)
    {
        NumAvgSamples = count;
    }
}
