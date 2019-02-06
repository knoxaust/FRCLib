package frc.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import frc.robot.interfaces.*;
public class PathFollower{

	
private double lookForeward;
private EncoderGyro calc;
private PIDController left;
private PIDController right;
private Encoder rightEnc;
private Encoder leftEnc;
private double tolerance;
//before you can use this you need to create the drivetrain PID loops create an EncoderGyro class to send and 
public PathFollower(double width,PIDController left,PIDController right,Encoder leftEnc,Encoder rightEnc,double lookForeward,double tolerance){
    this.calc = new EncoderGyro(width);
    this.lookForeward=lookForeward;
    this.left = left;
    this.right = right;
    this.left.setAbsoluteTolerance(tolerance);
    this.right.setAbsoluteTolerance(tolerance);
    this.rightEnc = rightEnc;
    this.leftEnc =  leftEnc;
    this.tolerance = tolerance;
}


public void prefollow(){//this gets ran once before attempting to follow a path but not while a path is being followed
    rightEnc.reset();
    right.setSetpoint(0);
    leftEnc.reset();
    left.setSetpoint(0);
    calc.reset();
}

public boolean driveStraightForeward(double distance,double angle,double robotAngle){
    return forewardFollow(new DriveStright(distance, angle),leftEnc.getDistance(),rightEnc.getDistance(),robotAngle);
}

public boolean driveStraight(double distance,double angle,double robotAngle){
    return backwardsFollow(new DriveStright(distance, angle),leftEnc.getDistance(),rightEnc.getDistance(),robotAngle);
}

//this is for folowing a spline forewards
public boolean forewardFollow(Followable path,double distanceLeft,double distanceRight,double angle){
    double distance = (distanceRight + distanceLeft)/2;//the distance the robot has completed in its path
    double targetDistance, distanceMod;
    if (distance + lookForeward > path.getDistance()){
        targetDistance = path.getDistance();
        distanceMod = distance+lookForeward-path.getDistance();
    }else{
        targetDistance= distance + lookForeward;
        distanceMod = 0;
    } 
    double targetAngle = path.getReverseAngle(targetDistance);
    double deltaAngle;
    double targetDistanceRight;
    double targetDistanceLeft;
    
    //calculating the change in angle with casses for the -180 to 180 jump produced by a gyro and the spline pathing
    if (angle>90 && targetAngle<-90)
        deltaAngle = (180-angle) + (180+targetAngle);
    else if (angle<-90 && targetAngle>90)
        deltaAngle = (-180-angle)-(180-targetAngle);
    else 
        deltaAngle =targetAngle-angle;

    //calculating the distance each side of the robot will have to drive to stay on track
    targetDistanceLeft = distanceLeft + calc.angleToDistance(deltaAngle)-lookForeward+distanceMod;
    targetDistanceRight = distanceRight -calc.angleToDistance(deltaAngle)-lookForeward+distanceMod;

    //end follower if completed
    if (Math.abs(distance-path.getDistance())<tolerance){
        prefollow();
        return true;
    }else{//the path was not complete so we need to tell it to continue to follow
        left.setSetpoint(targetDistanceLeft);
        right.setSetpoint(targetDistanceRight);
        return false;
    }
}


    //this is for following a spline backwards
    public boolean backwardsFollow(Followable path,double distanceLeft,double distanceRight,double angle){
        double distance = Math.abs((distanceRight + distanceLeft)/2);//the distance the robot has completed in its path
    double targetDistance, distanceMod;
    if (distance + lookForeward > path.getDistance()){
        targetDistance = path.getDistance();
        distanceMod = distance+lookForeward-path.getDistance();
    }else{
        targetDistance= distance + lookForeward;
        distanceMod = 0;
    } 
    double targetAngle = path.getAngle(targetDistance);
    double deltaAngle;
    double targetDistanceRight;
    double targetDistanceLeft;
    
    //calculating the change in angle with casses for the -180 to 180 jump produced by a gyro and the spline pathing
    if (angle>90 && targetAngle<-90)
        deltaAngle = (180-angle) + (180+targetAngle);
    else if (angle<-90 && targetAngle>90)
        deltaAngle = (-180-angle)-(180-targetAngle);
    else 
        deltaAngle =targetAngle-angle;

    //calculating the distance each side of the robot will have to drive to stay on track
    targetDistanceLeft = distanceLeft + calc.angleToDistance(deltaAngle)+lookForeward-distanceMod;
    targetDistanceRight = distanceRight -calc.angleToDistance(deltaAngle)+lookForeward-distanceMod;

    //end follower if completed
    if(Math.abs(distance-path.getDistance())<tolerance){
        prefollow();
        return true;
    }else{//the path was not complete so we need to tell it to continue to follow
        left.setSetpoint(targetDistanceLeft);
        right.setSetpoint(targetDistanceRight);
        return false;
    }
}
    
    public void stopFollowing(){//run this at the end of auto or if you want to stop having the robot hold position
        right.disable();
        left.disable();
    }
}