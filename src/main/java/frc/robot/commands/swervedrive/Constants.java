package frc.robot.commands.swervedrive;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import frc.robot.consoles.Shuffler;
import java.util.Map;

public class Constants {

    private static ShuffleboardLayout m_preferencesLayout = Shuffler.constructLayout(Shuffler.m_driveTab, "Driver Preferences", 0, 2, 7, 4, 2, 2, "LEFT");

    public static GenericEntry entryForwardBackwardSpeed = m_preferencesLayout
        .addPersistent("Max Forward Backward Speed", 2.4)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 5))
        .getEntry();

    public static GenericEntry entryLeftRightSpeed = m_preferencesLayout
        .addPersistent("Max Left Right Speed", 1.2)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 5))
        .getEntry();

    public static GenericEntry entryRotationSpeed = m_preferencesLayout
        .addPersistent("Max Rotation Speed", 2.0)
        .withWidget(BuiltInWidgets.kNumberSlider)
        .withProperties(Map.of("min", 0, "max", 5))
        .getEntry();
}
