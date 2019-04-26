package frc.robot.tools;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * This class is the class which is used to retrieve the data that the field returns when the game begins in order to properly guide the
 * auto plan
 */
public class AutoPositionCheck
{
    private static String[] data; // The data that the field returns

    /**
     * This class gets and stores the status of the field from the Driver's stations
     */
    public static void getGameData()
    {
        String gameData = DriverStation.getInstance().getGameSpecificMessage();
        data = new String[3];
        for (int c = 0; c < gameData.length(); c++)
        {
            data[c] = String.valueOf(gameData.charAt(c));
        }
    }

    /**
     * Gets the state of the ally switch
     * 
     * @return Either "L" or "R" to say which side that the ally switch is on
     */
    public static String getAllySwitch()
    {
        return data[0];
    }

    /**
     * Gets the state of the scale
     * 
     * @return Either "L" or "R" to say which side that the scale is on
     */
    public static String getScale()
    {
        return data[1];
    }

    /**
     * Gets the state of the enemy switch
     * 
     * @return Either "L" or "R" to say which side that the enemy switch is on
     */
    public static String getEnemySwitch()
    {
        return data[2];
    }
}
