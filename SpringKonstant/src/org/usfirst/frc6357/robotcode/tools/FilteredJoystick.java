package org.usfirst.frc6357.robotcode.tools;

import org.usfirst.frc6357.robotcode.tools.filters.Filter;

/**
 * This class is a general class whose function is to filter out joystick inputs
 * It operates with a generic array that uses Filter class static methods to get
 * a filtered double based on which port is being filtered
 */

import edu.wpi.first.wpilibj.Joystick;

public class FilteredJoystick extends Joystick
{
    private static Filter[] filters = new Filter[16];
    
    /**
     * Default constructor, calls superclass constructor from Joystick
     * @param port the port of the joystick being passed
     */
    public FilteredJoystick(int port)
    {
        super(port);
    }
    
    /**
     * This method returns a filtered number from the requested port
     * The Filter used is determined from the static mapping
     * @param port the port to get the data from
     * @return the filtered data from the joystick port
     */
    public double getFilteredAxis(int port)
    {
        if(filters[port] != null)
        {
            return filters[port].filter(getRawAxis(port));
        }
        else 
        {
            return getRawAxis(port);
        }
    }
    
    /**
     * Sets a pair between a port and a filter for user's general control
     * @param port the port to pair with a filter
     * @param f the filter to pair with a port
     */
    public static void setFilter(int port, Filter f)
    {
        filters[port] = f;
    }
}
