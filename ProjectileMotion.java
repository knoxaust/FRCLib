public class ProjectileMotion {
    
    private double robotHeight, shotAngle, targetHeight, accel;
    public final static int INCH = 0, FEET = 1, METER = 2;
    public ProjectileMotion(double robotHeight, double shotAngle, double targetHeight,int units){
        this.robotHeight = robotHeight;
        this.shotAngle = Math.toRadians(shotAngle);
        this.targetHeight = targetHeight;
        if (units == INCH)
            accel = -32.1740*12;
        else if (units == FEET)
            accel =-32.1740;
        else 
            accel = -9.81;
    }

    public double getShooterHeight(){
        return robotHeight;
    }
    public double getShotAngle(){
        return shotAngle;
    }
    public double getTargetHeight(){
        return targetHeight;
    }

    public double getTargetVel(double distance){
        if (Math.cos(shotAngle)!= 0){
            double a= (targetHeight-robotHeight)-(Math.sin(shotAngle)*distance)/Math.cos(shotAngle);
            double c= -accel*Math.pow(distance,2)/2/Math.pow(Math.cos(shotAngle),2);
            if (-4*a*c<0 || a == 0)
                return -1;
            return Math.max(Math.sqrt(-4*a*c)/(2*a),-1*Math.sqrt(-4*a*c)/(2*a));
        }else
        return -1;
    }

}