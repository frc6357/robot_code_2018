package org.usfirst.frc6357.robotcode.tools.filters;

/*
 * Creates an exponential curve on the filter
 * 0 correlates to 0, and 1 correlates to 1
 * There are several potential options:
 * y=(e^x-1)/(e-1)
 * y=e^(1-1/x^2)
 * y=x^3
 * y=e^(1-1/x)
 * Some function of tanh
 */
public class ExponentialFilter extends Filter
{
    private double coefficient;

    /**
     * Default constructor, gives a coefficient of coef
     * 
     * @param coef
     *            the coefficient of the cubic function, must be greater than zero
     */
    public ExponentialFilter(double coef)
    {
        coefficient = Math.abs(coef);
    }

    /**
     * Filters the input into a more exponential form Currently uses x^3, but more
     * formulas are available
     * 
     * @param rawAxis
     *            the data to be read in, from -1 to 1
     * @return the cubic relation of that data
     */
    @Override
    public double filter(double rawAxis)
    {
        return coefficient * Math.pow(rawAxis, 3);
    }

    /**
     * Sets the coefficient of the cubic function
     * 
     * @param c
     *            the coefficient, which must be greater than zero
     */
    public void setCoef(double c)
    {
        coefficient = Math.abs(c);
    }
}
