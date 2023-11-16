package frc.robot.commands.swervedrive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.consoles.Logger;
import frc.robot.subsystems.SwerveDriver;

import static frc.robot.BotSensors.gyro;

public class TimedSwerve extends CommandBase {

    private final double CORRECTION_TOLERANCE = 1;
    private final double CORRECTION_SPEED = 0.1;

    private SwerveDriver m_swerveDriver;
    private double m_xSpeed;
    private double m_ySpeed;
    private double m_turningSpeed;
    private double m_targetTime;
    private Timer m_timer;
    private double m_startingHeading;
    private double m_rampTime;


    public TimedSwerve(SwerveDriver swerveDriver, double xSpeed, double ySpeed, double turningSpeed,  double timeInSeconds, double rampTime) {
        Logger.setup("Constructing Command: TimedSwerve...");

        // Add given subsystem requirements
        m_swerveDriver = swerveDriver;
        m_targetTime = timeInSeconds;
        m_xSpeed = xSpeed;
        m_ySpeed = ySpeed;
        m_turningSpeed = turningSpeed;
        m_rampTime = rampTime;
        m_timer = new Timer();
        addRequirements(m_swerveDriver);
    }

    @Override
    public void initialize() {
        Logger.action(String.format("Initializing Command: TimedSwerve (xSpeed=%.2f, ySpeed=%.2f, turningSpeed=%.2f, time=%.1f) ...", m_xSpeed, m_ySpeed, m_turningSpeed, m_targetTime));
        m_timer.reset();
        m_timer.start();

        m_startingHeading = gyro.getYaw();

    }

    @Override
    public void execute() {
        //Yaw Correction
        double yawDifference = m_startingHeading - gyro.getYaw();
        double newTurningSpeed = m_turningSpeed;
        if(m_turningSpeed == 0){
            /*if(yawDifference < -CORRECTION_TOLERANCE){
                newTurningSpeed -= CORRECTION_SPEED;
            }else if(yawDifference > CORRECTION_TOLERANCE){
                newTurningSpeed += CORRECTION_SPEED;
            }*/
            newTurningSpeed = yawDifference * 0.06;
        }     

        double xSpeedTwo = 0;
        double ySpeedTwo = 0;
        //Ramp time
        double currentTime = m_timer.get();
        if(currentTime < m_rampTime){
            xSpeedTwo = m_xSpeed * (currentTime / m_rampTime); 
            ySpeedTwo = m_ySpeed * (currentTime / m_rampTime); 
        }else if(currentTime > m_targetTime - m_rampTime){
            xSpeedTwo = m_xSpeed * (m_targetTime - currentTime / m_rampTime); 
            ySpeedTwo = m_ySpeed * (m_targetTime - currentTime / m_rampTime); 
        }else{
            xSpeedTwo = m_xSpeed;
            ySpeedTwo = m_ySpeed;
        }

        //Clamp the speeds based on sign
        if(m_xSpeed < 0){
            if(xSpeedTwo > 0){
                xSpeedTwo = 0;
            }
        }else if(m_xSpeed > 0){
            if(xSpeedTwo < 0){
                xSpeedTwo = 0;
            }
        }
        if(m_ySpeed < 0){
            if(ySpeedTwo > 0){
                ySpeedTwo = 0;
            }
        }else if(m_ySpeed > 0){
            if(ySpeedTwo < 0){
                ySpeedTwo = 0;
            }
        }

        Logger.info("Current Yaw: " + gyro.getYaw() + " Yaw Difference: " + yawDifference + " Initial Yaw: " + m_startingHeading + " Turning Speed: " + newTurningSpeed);

        m_swerveDriver.setChassisSpeed(xSpeedTwo, ySpeedTwo, newTurningSpeed);

    }

    @Override
    public boolean isFinished() {
        double currentTime = m_timer.get();

        if (currentTime > m_targetTime) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        double currentTime = m_timer.get();
        if (interrupted) {
            Logger.ending(String.format("Interrupting Command: TimedSwerve... Current Time: %.2f", currentTime));
        } else {
            Logger.ending(String.format("Ending Command: TimedSwerve... Current Time: %.2f", currentTime));
        }
        m_swerveDriver.stopModules();
    }

}
