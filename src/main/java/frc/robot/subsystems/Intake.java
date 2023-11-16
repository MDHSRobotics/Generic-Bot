package frc.robot.subsystems;

import java.util.Map;
import com.revrobotics.CANSparkMax.IdleMode;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.BotControllers;

public class Intake extends GenericSubsystem {

    public Intake() {
        super(Map.of("sparkMaxIntake", 36, "sparkMaxIntakeTwo", 37));

        super.setCANSparkMaxBrakeMode("Intake", IdleMode.kBrake);
        super.setCANSparkMaxBrakeMode("IntakeTwo", IdleMode.kBrake);

        super.follow("Intake", "IntakeTwo", true);
    }

    public CommandBase runIntakeCommand() {
        return this.run(() -> super.move("Intake", BotControllers.xbox1.xbox.getRightTriggerAxis()- BotControllers.xbox1.xbox.getLeftTriggerAxis(), 0.1));
    }


}