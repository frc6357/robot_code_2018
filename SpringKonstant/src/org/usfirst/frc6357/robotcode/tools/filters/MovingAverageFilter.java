package org.usfirst.frc6357.robotcode.tools.filters;

/**
 * This class creates a filter which has active dampening The function
 * essentially works by creating a moving average and adjusting the returned
 * value based on this average.
 */
public class MovingAverageFilter extends Filter
{
    private int MAX_VALUES;
    private double average;
    private boolean firstPass;

    /**
     * Constructor for an object with the given number of maximum values
     * 
     * @param max
     *            the maximum number of values to be passed
     */
    public MovingAverageFilter(int max)
    {
        MAX_VALUES = max;
    }

    /**
     * Filter which uses a moving average to calculate the rate of acceleration
     * Primarily exists for gentler acceleration and reduced slippage
     * 
     * @param rawAxis
     *            the data to be passed in, from -1 to 1
     * @return the filtered data, which is generated with a moving average
     */
    @Override
    public double filter(double rawAxis)
    {
        if (firstPass)
        {
            average = rawAxis;
            firstPass = false;
        } else
        {
            average -= average / MAX_VALUES;
            average += rawAxis / MAX_VALUES;
        }

        return average;
    }

    /**
     * Sets the number of maximum values
     * 
     * @param m
     *            the new number of maximum values
     */
    public void setMaxValues(int m)
    {
        MAX_VALUES = m;
    }
}
