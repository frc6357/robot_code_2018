package org.usfirst.frc6357.robotcode.tools.filters;
/**
 * This class is a filter that has a deadband for it's return
 * If a value is filtered and in the deadband, it returns zero
 * Otherwise, the scale is readjusted to have a slope from y=0 to y=1 starting at x=deadband
 */
public class FilterDeadband extends Filter
{
    private double deadband;    //The deadbanding for the input, equal to distance from zero
    private double slope;       //The slope m of the y=mx line to get careful control
    private double gain;        //The gain is the maximum value for a graph, and is by default 1
    
    /**
     * The consructor for a filter with a deadband
     * @param deadband the deadbanding zone, equivalent to the distance from zero
     */
    public FilterDeadband(double deadband)
    {
        gain = 1;
        setDeadband(deadband);
    }
    
    /**
     * The constructor which allows a user to specify gain
     * @param deadband deadband the deadbanding zone, equivalent to the distance from zero
     * @param gain the gain for which to set the max value
     */
    public FilterDeadband(double deadband, double gain)
    {
        setDeadband(deadband);
        this.gain = gain;
    }

    /**
     * Filters the raw input for a deadbanding zone
     * If the given value is inside the deadband, zero is returned
     * Otherwise, a scale is implemented and slope is readjusted for careful control
     * @param rawAxis the data to be passed in, from -1 to 1
     */
    @Override
    public double filter(double rawAxis)
    {
        if(!(Math.abs(rawAxis) < deadband))
        {
            return (slope * (rawAxis - deadband)) * gain;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Sets the deadband of the filter to a new deadband, with length equal to distance to zero
     * Also adjust the slope of the y=mx line for better functionality
     * @param deadband
     */
    public void setDeadband(double deadband)
    {
        this.deadband = deadband;
        slope = 1 / (1 - deadband);
    }
    
    /**
     * Sets the maximum out put of the filter
     * @param gain the new gain to adjust output
     */
    public void setGain(double gain)
    {
        this.gain = gain;
    }
}
