package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.consoles.Logger;
import frc.robot.subsystems.SwerveDriver;

import static frc.robot.BotSensors.gyro;

public class TurnDegrees extends CommandBase{

    private final double CORRECTION_TOLERANCE = 0.7;

    private double turning_speed = 0.7;

    private SwerveDriver m_swerveDriver;
    private double m_degrees;
    private double m_targetHeading;

    public TurnDegrees(SwerveDriver swerveDriver, double degrees) {
        Logger.setup("Constructing Command: TurnDegrees...");

        m_swerveDriver = swerveDriver;
        m_degrees = degrees;
        addRequirements(m_swerveDriver);
    }

    @Override
    public void initialize() {
        Logger.action("Initializing Command: TurnDegrees");

        m_targetHeading = gyro.getYaw() + m_degrees;
        if(m_targetHeading > 180){
            m_targetHeading = -180 + (m_targetHeading - 180);
        }
        Logger.info("Target Heading: " + m_targetHeading + " Current Heading: " + gyro.getYaw());
    }

    @Override
    public void execute() {
        double yawDifference = m_targetHeading - gyro.getYaw();
        if(yawDifference < 20 && yawDifference > -20){
            turning_speed = 0.2;
        }

        if(m_targetHeading < gyro.getYaw()){
            m_swerveDriver.setChassisSpeed(0, 0, -turning_speed);
            Logger.info("Target Heading: " + m_targetHeading + "Current Heading: " + gyro.getYaw());
        }else if(m_targetHeading > gyro.getYaw()){
            m_swerveDriver.setChassisSpeed(0, 0, turning_speed);
            Logger.info("Target Heading: " + m_targetHeading + "Current Heading: " + gyro.getYaw());
        }

    }

    @Override
    public boolean isFinished() {
         
        double yawDifference = m_targetHeading - gyro.getYaw();
        if(yawDifference < CORRECTION_TOLERANCE && yawDifference > -CORRECTION_TOLERANCE){
            Logger.info("Ending" + yawDifference);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            Logger.ending("Interrupting Command: TurnDegrees...");
        } else {
            Logger.ending("Ending Command: TurnDegrees...");
        }
        m_swerveDriver.stopModules();
    }

}
