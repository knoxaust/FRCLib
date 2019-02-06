package frc.robot;

public class EncoderGyro {
	private double angle;
	private double radius;
	private double startingDistanceRight;
	private double startingDistanceLeft;
	public EncoderGyro(double width, double startingDistanceL,double startingDistanceR){
		radius = width/2;
		startingDistanceLeft = startingDistanceL;
		startingDistanceRight = startingDistanceR;
	}
	public EncoderGyro(double width){
		radius = width/2;
		startingDistanceLeft = 0;
		startingDistanceRight = 0;
	}
	public double updateAngle(double leftDistance,double rightDistance){
		double wheelArc = ((leftDistance - startingDistanceLeft) - (rightDistance - startingDistanceRight)) / 2;
		double radians = wheelArc/radius;
		angle = Math.toDegrees(radians);
		angle %= 360;
		if (angle > 180)
			angle = (angle%180)-180;
		if (angle < -180)
			angle = (angle%180)+180;
		return angle;
	}
	public double angleToDistance(double angle){
		double radians = Math.toRadians(angle);
		return radians*radius;
	}
	public double getAngle(){
		return angle;
	}
	public double getRadius(){
		return radius;
	}
    public void reset(){
        startingDistanceLeft = 0;
        startingDistanceRight = 0;
    }
}