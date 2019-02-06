package frc.robot;

import frc.robot.interfaces.Followable;

/**
 * DriveStright
 */
public class DriveStright implements Followable{
    private double distance,angle;
    public DriveStright(double distance, double angle){
        this.distance = distance;
        this.angle = angle;
    }
    
	@Override
	public double getAngle(double distance) {
		return angle;
	}

	@Override
	public double getReverseAngle(double distance) {
		return angle;
	}

	@Override
	public double getDistance() {
		return distance;
	}

}