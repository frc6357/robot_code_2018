//
///*package frc.robot.subsystems;
//
//import frc.robot.Ports;
//import frc.robot.Robot;
//import frc.robot.subsystems.ArmSystem.ArmState;
//
//import com.mindsensors.CANLight;
//
//import edu.wpi.first.wpilibj.DriverStation;
//import edu.wpi.first.wpilibj.command.Subsystem;
//
///**
// *
// */
//public class LEDs extends Subsystem 
//{
//
//    // For LEDs
//    public static CANLight lights;
//    public static DriverStation ds;
//    
//    public LEDs()
//    {
//        // LED lights with driver station
//        lights = new CANLight(Ports.CANLeds);
//        ds = DriverStation.getInstance();
//        lights.writeRegister(1, 0.25, 255, 0, 255);
//    }
//    
//    public void periodic()
//    {
//        if(Robot.armSystem.getArmShoulderState() == ArmState.UP)
//        lights.flash(1);
//    
//        if(Robot.driveBaseSystem.isStrafeDeployed())
//        {
//            lights.showRGB(0, 255, 0);    // Green
//        }
//        else if (ds.getAlliance() == DriverStation.Alliance.Red) 
//        {
//            lights.showRGB(255, 0, 0);    // Red
//        }
//        else if (ds.getAlliance() == DriverStation.Alliance.Blue) 
//        {
//            lights.showRGB(0, 0, 255);      // Blue
//        }
//        else if (ds.getAlliance() == DriverStation.Alliance.Invalid) 
//        {
//            lights.showRGB(255, 200, 0);    // Yellow
//        }
//    }
//    
//    public void randomColor()
//    {
//        int r = (int) (Math.random() * 255);
//        int g = (int) (Math.random() * 255);
//        int b = (int) (Math.random() * 255);
//        
//        lights.showRGB(r, g, b);
//    }
//    
//    @Override
//    protected void initDefaultCommand()
//    {
//        // TODO Auto-generated method stub
//        
//    }
//    
//}
//*/
