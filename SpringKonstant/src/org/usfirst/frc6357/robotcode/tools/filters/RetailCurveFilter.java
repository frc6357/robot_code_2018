package org.usfirst.frc6357.robotcode.tools.filters;
/**
 * This filter follows a retail curve
 * The variable coefficient will specify the degree of the retail curve
 * It must be greater than zero and less than ten
 * The retail formula follows the formula:
 * f(x) = x*(c/9) + x^5 * (9-c)/9
 */
public class RetailCurveFilter extends Filter
{
    private double coefficient, gain;
    
    /**
     * Constructor which generates a retail curve with the degree of coef (affects curvature)
     * @param coef the coefficient of the retail curve (affects curvature)
     */
    public RetailCurveFilter(double coef)
    {
        gain = 1;
        coefficient = coef;
    }
    
    /**
     * Overloaded constructor that allows the user to affect both curvature and max value
     * @param coef the coefficient with which to adjust curvature
     * @param gain the gain which which to adjust max value
     */
    public RetailCurveFilter(double coef, double gain)
    {
        this.gain = gain;
        coefficient = coef;
    }
    
    /**
     * Generates filtered data that follows a retail curve
     * @param rawAxis the data to be passed in, from -1 to 1
     * @return the newly filtered data from -1 to 1
     */
    @Override
    public double filter(double rawAxis)
    {
        return gain * (rawAxis * coefficient/9 + Math.pow(rawAxis, 5) * (9-coefficient)/9);
    }

    /**
     * Sets the coefficient to a new variable
     * @param c the coefficient to be passed, between 0 and 10
     */
    public void setCoef(double c)
    {
        if(Math.abs(c) > 10) return;
        coefficient = c;
    }
    
    /**
     * Sets the gain to a new parameter
     * @param g the new gain with which to adjust output
     */
    public void setGain(double g)
    {
        gain = g;
    }
}
