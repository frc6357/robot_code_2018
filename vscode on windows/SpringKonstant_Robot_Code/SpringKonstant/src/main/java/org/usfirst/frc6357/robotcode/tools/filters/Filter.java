package org.usfirst.frc6357.robotcode.tools.filters;

/**
 * This class is an abstract generic for individual filters It lays out the groundwork for any filtering to be done by subclasses.
 */
public abstract class Filter
{
    /**
     * This is an abstraction for the filter method, and all Filters will use this implementation
     * 
     * @param rawAxis
     *            the actual value being returned by the raw data
     * @return the filtered data to be passed to the motor
     */
    public abstract double filter(double rawAxis);
}
