package frc.robot;
import frc.robot.interfaces.*;
import java.util.ArrayList;

public class RobotGrid implements Followable{
//NO ARCS OVER 90 DEGREES
    private ArrayList<Position> path;
    private double precision;
    private ArrayList<String> codeSave;
//NO ARCS OVER 90 DEGREES
    public RobotGrid(double x, double y, double angle, double precision) {//precsionis in points per degree
        path = new ArrayList<Position>();
        codeSave = new ArrayList<String>();
        path.add(new Position(x, y, angle));
        codeSave.add("RobotGrid grid = new RobotGrid(" + x + "," + y + "," + angle + "," + precision + ");");
        this.precision = precision;
    }
    public RobotGrid(double x, double y, double angle, double precision, double distance){
    	path = new ArrayList<Position>();
        path.add(new Position(x, y, angle, distance));
        this.precision = precision;
    }
    //NO ARCS OVER 90 DEGREES
    public void addLinearPoint(double x, double y, double angle) {//when initalizing x and y do not matter it is just a way to set the zero of the field and that zero can be wherever you want it to be
        if (angle >180)
            angle = -angle%180;
        path.add(new Position(x, y, angle, Math.sqrt(Math.abs(x-path.get(path.size()-1).getX())*Math.abs(x-path.get(path.size()-1).getX())+Math.abs(y-path.get(path.size()-1).getY())*Math.abs(y-path.get(path.size()-1).getY()))+path.get(path.size()-1).getDistance()));
        codeSave.add("grid.addLinearPoint(" + x + "," + y + ","+angle + ");");
    }
//NO ARCS OVER 90 DEGREES


    public void addPoint(double x, double y, double angle) {//attempt to fix bad intercept values
        Position p1 = path.get(path.size() - 1);
        Position p2;
        Position p3 = new Position(x, y, angle);
        double tempX;
        double tempY;
        codeSave.add("grid.addPoint(" + x + "," + y + ","+angle + ");");
        if (Math.abs(p3.getAngle())==90){
            tempX = p3.getX();
            tempY = p1.getSlope() * (tempX - p1.getX()) + p1.getY();
        }else if (Math.abs(p1.getAngle())==90){
            tempX = p1.getX();
            tempY = p3.getSlope() * (tempX - p3.getX()) + p3.getY();
        }else {
            tempX = p3.intersectX(p1);
            tempY = p3.getSlope() * (tempX - p3.getX()) + p3.getY();
        }

        p2 = new Position(tempX, tempY);
        System.out.println(p2.toString());
        double pointAngle = p1.getAngle();
        for (double t = 1/(precision * Math.abs(angle-p1.getAngle()));t<=1;t +=1/(precision * Math.abs(angle-p1.getAngle()))){
            double pointX = Math.pow(1- t, 2)*p1.getX()+2*(1-t)*t*p2.getX()+Math.pow(t, 2)*p3.getX();
            double pointY = Math.pow(1- t, 2)*p1.getY()+2*(1-t)*t*p2.getY()+Math.pow(t, 2)*p3.getY();
            pointAngle = Math.toDegrees(Math.atan2(pointY-path.get(path.size()-1).getY(),pointX-path.get(path.size()-1).getX()));
            path.add(new Position(pointX,pointY,pointAngle, Math.sqrt(Math.pow(pointX-path.get(path.size()-1).getX(),2) + Math.pow(pointY-path.get(path.size()-1).getY(),2))+path.get(path.size()-1).getDistance()));
        }
    }
    public void addPoint(double x,double y, double angle, double x2, double y2){
    	Position p1 = path.get(path.size()-1);
    	Position p2 = new Position(x2,y2);
    	Position p3 = new Position(x,y,angle);
        double pointAngle = p1.getAngle();
        codeSave.add("grid.addPoint(" + x + "," + y + ","+angle + "," + x2 + "," + y2 + ");");
    	for (double t = 1/(precision * Math.abs(angle-p1.getAngle()));t<=1;t +=1/(precision * Math.abs(angle-p1.getAngle()))){
            double pointX = Math.pow(1- t, 2)*p1.getX()+2*(1-t)*t*p2.getX()+Math.pow(t, 2)*p3.getX();
            double pointY = Math.pow(1- t, 2)*p1.getY()+2*(1-t)*t*p2.getY()+Math.pow(t, 2)*p3.getY();
            pointAngle = Math.toDegrees(Math.atan2(pointY-path.get(path.size()-1).getY(),pointX-path.get(path.size()-1).getX()));
            path.add(new Position(pointX,pointY,pointAngle, Math.sqrt(Math.pow(pointX-path.get(path.size()-1).getX(),2) + Math.pow(pointY-path.get(path.size()-1).getY(),2))+path.get(path.size()-1).getDistance()));
        }
    }
    public void addRelativePoint(double distance, double angle){
    	if(distance > .1 + getDistance()){
    		path.add(new Position(distance,angle,true));    		
    	}
    }
    public void sCurve(double targetX, double targetY, double x1, double y1, double x2, double y2){
    	Position p0 = path.get(path.size()-1);
        double pointAngle = p0.getAngle();
        codeSave.add("grid.sCurve(" + targetX + "," + targetY + "," + x1 + "," + y1 + "," + x2 + "," + y2 + ");");
    	for (double t = 1/(180.0*precision) ; t<=1;t+=1/(180.0*precision)){
    		double pointX = Math.pow(1-t,3)*p0.getX()+3*Math.pow(1-t,2)*t*x1+3*(1-t)*Math.pow(t, 2)*x2+Math.pow(t, 3)*targetX;
    		double pointY = Math.pow(1-t,3)*p0.getY()+3*Math.pow(1-t,2)*t*y1+3*(1-t)*Math.pow(t, 2)*y2+Math.pow(t, 3)*targetY;
    		pointAngle = Math.toDegrees(Math.atan2(pointY-path.get(path.size()-1).getY(),pointX-path.get(path.size()-1).getX()));
            path.add(new Position(pointX,pointY,pointAngle, Math.sqrt(Math.pow(pointX-path.get(path.size()-1).getX(),2) + Math.pow(pointY-path.get(path.size()-1).getY(),2))+path.get(path.size()-1).getDistance()));
    	}
    }
    public void sCurve(double targetX,double targetY){
        Position p0 = path.get(path.size()-1);
        Position midline = new Position((targetX+p0.getX())/2, (targetY+p0.getX())/2, p0.getAngle()+90);
        Position endpoint = new Position(targetX,targetY,p0.getAngle());
        double x2 = midline.intersectX(endpoint);
        double y2;
        if(midline.getAngle()==90)
            y2 = endpoint.getSlope() * (x2 - endpoint.getX()) + endpoint.getY();
        else
            y2 = midline.getSlope() * (x2 - midline.getX()) + midline.getY();
        double x1 = midline.intersectX(p0);
        double y1;
        if(midline.getAngle()==90)
            y1 = p0.getSlope() * (x1 - p0.getX()) + p0.getY();
        else
            y1 = midline.getSlope() * (x1 - midline.getX()) + midline.getY();
            codeSave.add("grid.sCurve(" + targetX + "," + targetY +");");
    	for (double t = 1/(180.0*precision) ; t<=1;t+=1/(180.0*precision)){
    		double pointX = Math.pow(1-t,3)*p0.getX()+3*Math.pow(1-t,2)*t*x1+3*(1-t)*Math.pow(t, 2)*x2+Math.pow(t, 3)*targetX;
    		double pointY = Math.pow(1-t,3)*p0.getY()+3*Math.pow(1-t,2)*t*y1+3*(1-t)*Math.pow(t, 2)*y2+Math.pow(t, 3)*targetY;
    		double pointAngle = Math.toDegrees(Math.atan2(pointY-path.get(path.size()-1).getY(),pointX-path.get(path.size()-1).getX()));
            path.add(new Position(pointX,pointY,pointAngle, Math.sqrt(Math.pow(pointX-path.get(path.size()-1).getX(),2) + Math.pow(pointY-path.get(path.size()-1).getY(),2))+path.get(path.size()-1).getDistance()));
    	}
    }
    public void addCurve(ArrayList <Position> pos,int points){//needs testing and is impractical but its fun
        pos.add(0,path.get(path.size()-1));
        int n = pos.size()-1;
        for (double t = 1.0/points ; t<=1;t+=1.0/points){
            double pointX = 0;
            double pointY = 0;
            for(int i = 0;i <pos.size();i++){
                pointX += choose(n,i)*Math.pow(1-t,n-i)*Math.pow(t,i)*pos.get(i).getX();
                pointY += choose(n,i)*Math.pow(1-t,n-i)*Math.pow(t,i)*pos.get(i).getY();
            }
            double pointAngle = Math.toDegrees(Math.atan2(pointY-path.get(path.size()-1).getY(),pointX-path.get(path.size()-1).getX()));
            path.add(new Position(pointX,pointY,pointAngle, Math.sqrt(Math.pow(pointX-path.get(path.size()-1).getX(),2) + Math.pow(pointY-path.get(path.size()-1).getY(),2))+path.get(path.size()-1).getDistance()));
        }
    }
    //NO ARCS OVER 90 DEGREES
    public String toString() {
        String temp = "";
        for (Position pos : path) {
            temp += pos.toString() + "\n";
        }
        return temp;
    }
    public String toCodeRelative(){
    	String temp = "";
        for (Position pos : path) {
            temp += pos.toCode() + "\n";
        }
        return temp;
    }

    public String toCode(){
        String temp = "";
        for (String code : codeSave){
            temp += code + "\n";
        }
        return temp;
    }
//NO ARCS OVER 90 DEGREES
//FIXME
/*    private double atan3(double tan,double angle){
        double value = Math.toDegrees(tan);
        if (angle <90 && angle >= 0 )
            return value;
        if (angle >= 90 && angle < 180)
            return 180-value;
        if (angle >= -180 && angle <= -90)
            return -180 + value;
        if (angle > -90)
            return -value;
        return 360;
    }*/
    // average of both sides not just one
    @Override
    public double getAngle(double distance){
        for (int i = 1; i < path.size()-1; i++){
            if(path.get(i).getDistance() > distance){
                return path.get(i-1).getAngle();
            }
            if (i>2){
                path.remove(i-2);
                i--;
            }
        }
        return path.get(path.size()-1).getAngle();
    }
    @Override
    public double getReverseAngle(double distance){
    	double angle = getAngle(distance);
    	if (angle >= 0)
    		angle -= 180;
    	else
    		angle += 180;
    	return angle;
    }
    @Override
    public double getDistance(){
        return path.get(path.size()-1).getDistance();
    }
    public double speedMultiplier(double distance, double angle, double speed){
    	double angleTarget;
    	if(speed > 0)
    		angleTarget = getAngle(distance + 25);
    	else 
    		angleTarget = getReverseAngle(distance + 25);

    	return Math.abs(1-Math.abs((angleTarget-angle)/115/1.66))*speed;
    }
    public double getLeftIPS(double distance,double angle,double baseIPS, EncoderGyro change){//inches per second
    	double t = 10/baseIPS;
    	double leftDistance;
    	double angleChange;
    	if(baseIPS>0){
    		if (getAngle(distance+10)>90 && angle < -90){
     			angleChange = 180-getAngle(distance+10);
    			angleChange += 180 + angle;
    		}else if(angle > 90 && getAngle(distance + 10) < -90){
    			angleChange = 180-angle;
    			angleChange += 180 + getAngle(distance +10);
    		}else angleChange = getAngle(distance +10)-angle;
    	}else {
    		if (getReverseAngle(distance+10)>90 && angle < -90){
        		angleChange = 180-getReverseAngle(distance+10);
        		angleChange += 180 + angle;
        	}else if(angle > 90 && getReverseAngle(distance + 10) < -90){
        		angleChange = 180-angle;
        		angleChange += 180 + getReverseAngle(distance +10);
        	}else angleChange = getReverseAngle(distance +10)-angle;
    	}
    	leftDistance = change.angleToDistance(angleChange)+10;
    	return leftDistance /t;
    }
    public double getRightIPS(double distance,double angle,double baseIPS, EncoderGyro change){//inches per second
    	double t = 10/baseIPS;
    	double rightDistance;
    	double angleChange;
    	if(baseIPS>0){
    		if (getAngle(distance+10)>90 && angle < -90){
    		angleChange = 180-getAngle(distance+10);
    		angleChange += 180 + angle;
    		}else if(angle > 90 && getAngle(distance + 10) < -90){
    		angleChange = 180-angle;
    		angleChange += 180 + getAngle(distance +10);
    		}else angleChange = getAngle(distance +10)-angle;
    	}else {
    		if (getReverseAngle(distance+10)>90 && angle < -90){
        		angleChange = 180-getReverseAngle(distance+10);
        		angleChange += 180 + angle;
        	}else if(angle > 90 && getReverseAngle(distance + 10) < -90){
        		angleChange = 180-angle;
        		angleChange += 180 + getReverseAngle(distance +10);
        	}else angleChange = getReverseAngle(distance +10)-angle;
    	}
    	rightDistance =  change.angleToDistance(angleChange)-10;
    	return rightDistance /t;
    }

    private int factorial(int n){
        if (n<=1)
        return 1;
        return n*factorial(n-1);
    }
    private double choose(int n,int i){
        return factorial(n)/(double)(factorial(i)*factorial(n-i));
    }
}