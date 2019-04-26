package frc.robot.tools.filters;

/**
 * This class creates a filter which has active dampening The function essentially works by creating a moving average and adjusting the
 * returned value based on this average.
 */
public class MovingAverageFilter extends Filter
{
    private int MAX_VALUES;
    private double average, gain;
    private boolean firstPass;

    /**
     * Constructor for an object with the given number of maximum values
     * 
     * @param max
     *            the maximum number of values to be passed
     */
    public MovingAverageFilter(int max)
    {
        gain = 1;
        MAX_VALUES = max;
    }

    /**
     * Constructor which allows you to pass both mmaximum number of values and gain
     * 
     * @param max
     *            the max number of values calculated in the average
     * @param g
     *            the gain to which you set the filter
     */
    public MovingAverageFilter(int max, double g)
    {
        MAX_VALUES = max;
        gain = g;
    }

    /**
     * Filter which uses a moving average to calculate the rate of acceleration Primarily exists for gentler acceleration and reduced
     * slippage
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
        }
        else
        {
            average -= average / MAX_VALUES;
            average += rawAxis / MAX_VALUES;
        }
        return average * gain;
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

    /**
     * Sets the gain to a new parameter
     * 
     * @param g
     *            the new gain with which to adjust output
     */
    public void setGain(double g)
    {
        gain = g;
    }
}
