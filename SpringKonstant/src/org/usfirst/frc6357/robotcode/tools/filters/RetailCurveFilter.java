package org.usfirst.frc6357.robotcode.tools.filters;

/**
 * This filter follows a retail curve The variable coefficient will specify the
 * degree of the retail curve It must be greater than zero and less than ten The
 * retail formula follows the formula: f(x) = x*(c/9) + x^5 * (9-c)/9
 */
public class RetailCurveFilter extends Filter
{
    private double coefficient;

    /**
     * Constructor which generates a retail curve with the degree of coef
     * 
     * @param coef
     *            the coefficient of the retail curve
     */
    public RetailCurveFilter(double coef)
    {
        coefficient = coef;
    }

    /**
     * Generates filtered data that follows a retail curve
     * 
     * @param rawAxis
     *            the data to be passed in, from -1 to 1
     * @return the newly filtered data from -1 to 1
     */
    @Override
    public double filter(double rawAxis)
    {
        return rawAxis * coefficient / 9 + Math.pow(rawAxis, 5) * (9 - coefficient) / 9;
    }

    /**
     * Sets the coefficient to a new variable
     * 
     * @param c
     *            the coefficient to be passed, between 0 and 10
     */
    public void setCoef(double c)
    {
        if (Math.abs(c) > 10)
            return;
        coefficient = c;
    }
}
