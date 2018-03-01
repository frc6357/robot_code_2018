package org.usfirst.frc6357.robotcode.tools;

import edu.wpi.first.wpilibj.DriverStation;

public class AutoPositionCheck
{
	private static String[] data;
	
	public static void getGameData()
	{
		String gameData = DriverStation.getInstance().getGameSpecificMessage();	
		data = new String[3];
		for(int c=0; c<gameData.length(); c++)
		{
			data[c] = String.valueOf(gameData.charAt(c));
		}
	}
	
	public static String getAllySwitch()
	{
		return data[0];
	}
	
	public static String getScale()
	{
		return data[1];
	}
	
	public static String getEnemySwitch()
	{
		return data[2];
	}
}
