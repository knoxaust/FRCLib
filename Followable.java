package frc.robot.interfaces;

public interface Followable {
    public double getAngle(double distance);
    public double getReverseAngle(double distance);
    public double getDistance();
}