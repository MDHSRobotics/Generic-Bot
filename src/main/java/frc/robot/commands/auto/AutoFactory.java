package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.BotSubsystems;
import frc.robot.commands.swervedrive.TimedSwerve;

/* This class factory will create a command group based on the starting location
    and target placement of the cube (eject on bottom or place on top shelf)
*/
public class AutoFactory extends SequentialCommandGroup {

    public AutoFactory(String location) {

        // STEP 1: Place the cube
        //addCommands(BotCommands.timedIntake);

        // STEP 2: Balance or move out of community
        
        switch (location){
            case "Center":
                // We're located in the center so balance on the charge station
                addCommands(
                    new EjectCube(BotSubsystems.intake, 1),
                
                    new BalanceChargeStation(BotSubsystems.swerveDriver)
                );
                break;
        
            //This is which direction the robot needs to move to clear the charge station
            case "Left":
            case "Right":
                double sideToSideSpeed = (location == "Left") ? -AutoConstants.DEFAULT_DRIVE_SPEED : AutoConstants.DEFAULT_DRIVE_SPEED;
                addCommands(
                    
                    new ParallelCommandGroup(
                        new TimedSwerve(BotSubsystems.swerveDriver, -AutoConstants.DEFAULT_DRIVE_SPEED,0 , 0, 0.3,0.1),
                        
                        BotSubsystems.forklift.runElevatorTime(0.4, -1)
                    ),

                    BotSubsystems.forklift.runExtenderTime(1.05, -1),

                    BotSubsystems.forklift.runWristTime(0.6, 1),

                    new TimedSwerve(BotSubsystems.swerveDriver, 0.2,0 , 0, 0.5,0.2),

                    BotSubsystems.forklift.openClampCommand(),

                    new WaitCommand(0.1),

                    new ParallelCommandGroup(
                        new TimedSwerve(BotSubsystems.swerveDriver, -0.2,0 , 0, 1,0.3),

                        BotSubsystems.forklift.runWristTime(0.6, -1)
                    ),

                    BotSubsystems.forklift.runExtenderTime(1.05, 1),

                    BotSubsystems.forklift.runElevatorTime(0.4, 1),
                
                    new TimedSwerve(BotSubsystems.swerveDriver, 0, sideToSideSpeed, 0, 0.5, 0),
                    
                    new WaitCommand(0.1),
                
                    //turn
                    //new TimedSwerve(BotSubsystems.swerveDriver, 0, 0, -0.7, 1.4, 0),
                    
                    new WaitCommand(0.1),

                    new TimedSwerve(BotSubsystems.swerveDriver, -AutoConstants.DEFAULT_DRIVE_SPEED,0 , 0, 3.5,0.5)
                );
                break;
            
            default:
                throw new java.lang.Error("Unkown Location; should be {Left, Center, Right}");           
        }

    }

}