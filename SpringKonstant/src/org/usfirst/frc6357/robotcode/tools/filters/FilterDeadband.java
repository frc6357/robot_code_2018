package org.usfirst.frc6357.robotcode.tools.filters;
/**
 * This class is a filter that has a deadband for it's return
 * If a value is filtered and in the deadband, it returns zero
 * Otherwise, the scale is readjusted to have a slope from y=0 to y=1 starting at x=deadband
 */
public class FilterDeadband extends Filter
{
    private double deadband;    //The deadbanding for the input, equal to distance from zero
    
    /**
     * The consructor for a filter with a deadband
     * @param deadband 
     */
    public FilterDeadband(double deadband)
    {
        this.deadband = deadband;
    }

    @Override
    public double filter(double rawAxis)
    {
        // TODO Auto-generated method stub
        return 0;
    }

}
