package frc.robot.commands.swervedrive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.consoles.Logger;
import frc.robot.subsystems.SwerveDriver;


public class ChangeSpeed extends CommandBase {

    private SwerveDriver m_swerveDriver;


    private boolean m_val = true;

    public ChangeSpeed(SwerveDriver swerveDriver, boolean val) {

        Logger.setup("Constructing Command: ChangeSpeed...");

        // Add given subsystem requirements
        m_swerveDriver = swerveDriver;
        m_val = val;
        addRequirements(m_swerveDriver);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void execute() {
        Logger.action("Executing Command: ChangeSpeed ..." + m_val);
        if(m_val){
            Constants.entryForwardBackwardSpeed.setDouble(2.4);
            Constants.entryLeftRightSpeed.setDouble(1.6);
            Constants.entryRotationSpeed.setDouble(2.0);
        }else if(!m_val){
            Constants.entryForwardBackwardSpeed.setDouble(0.5);
            Constants.entryLeftRightSpeed.setDouble(0.5);
            Constants.entryRotationSpeed.setDouble(0.95);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            Logger.ending("Interrupting Command: AlignGyro ...");
        } else {
            Logger.ending("Ending Command: AlignGyro ...");
        }
    }
    
}
