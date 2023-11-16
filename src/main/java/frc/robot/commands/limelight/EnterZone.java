package frc.robot.commands.limelight;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.consoles.Logger;
import frc.robot.sensors.Limelight;
import frc.robot.subsystems.SwerveDriver;

import static frc.robot.BotSensors.gyro;

public class EnterZone extends CommandBase {

    private SwerveDriver m_swerveDriver;

    private boolean yCorrect = false;
    private boolean xCorrect = false;

    public EnterZone(SwerveDriver swerveDriver) {

        Logger.setup("Constructing Command: EnterZone...");

        // Add given subsystem requirements
        m_swerveDriver = swerveDriver;
        addRequirements(m_swerveDriver);
    }

    @Override
    public void initialize() {
        Logger.action("Initializing Command: EnterZone ...");

        Limelight.setPipeline(0);
    }

    @Override
    public void execute() {
        
        double distance = Limelight.calculateDistanceToTarget();
        double xOffset = Limelight.getXOffset();

        /*double strafeSpeed = m_xOffsetPidController.calculate(xOffset);
        double forwardSpeed = m_distancePidController.calculate(distance);

        Logger.info("Distance: " + distance + "; xOffset: " + xOffset + "; strafeSpeed: " + strafeSpeed);
        m_swerveDriver.setChassisSpeed(strafeSpeed, -forwardSpeed, 0);*/
        double yawDifference = 0 - gyro.getYaw();
        double newTurningSpeed = 0;
        if(yawDifference < -1){
            newTurningSpeed -= 0.1;
        }else if(yawDifference > 1){
            newTurningSpeed += 0.1;
        }

        if(xOffset <= -14){
            Logger.info("moving left");
            m_swerveDriver.setChassisSpeed(0, 0.2, newTurningSpeed);
        }else if (xOffset >= -12){
            Logger.info("moving right");
            m_swerveDriver.setChassisSpeed(0, -0.2, newTurningSpeed);
        }else{
            yCorrect = true;
        }
        if(yCorrect){
            if(distance >= 6){
                Logger.info("moving forward");
                m_swerveDriver.setChassisSpeed(0.2, 0, newTurningSpeed);
            }else if (distance <= 3){
                Logger.info("moving backward");
                m_swerveDriver.setChassisSpeed(-0.2, 0, newTurningSpeed);
            }else{
                xCorrect = true;
            }
        }
    }

    @Override
    public boolean isFinished() {
        boolean atTarget;

        if (xCorrect && yCorrect){
            m_swerveDriver.stopModules();
            atTarget = true;
            //Limelight.setPipeline(1);
        } else {
            atTarget = false;
        }
        return atTarget;
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            Logger.ending("Interrupting Command: EnterZone ...");
        } else {
            Logger.ending("Ending Command: EnterZone ...");
        }
    }

}
