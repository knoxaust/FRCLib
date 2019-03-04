# FRCLib
These are the different library classes i have made over the years to acheive different controls tasks.
The encoder gyro class lets you get the angle of the robot without having a gyro as long as you have encoders on both sides of your drivetrain the param width is not the width of the robot but the inside to inside distance between the wheels so it can determine the turning radius

Followable is an interface that is required to use PathFollower

DriveStraight is a class called by PathFollower to make driving straight lines easier then using RobotGrid to path straight lines

RobotGrid is used to path complex curves used my PathFollower to make a robot follow the path

Position is a class used to store data for RobotGrid

PathFollower is a class that uses all of these classes together to follow paths


The robotGrid class is the main pathing class
The constructor has 4 parameters
the first of which is the distance the front of the robot is from the back wall
The second is the distance the middle of the robot is from the left wall 
The third is the angle of the robot relative to the anglethe robot starts at
The 4th being a precision value set it to something greater then 2 less then 50 but must be an not
example: RobotGrid nameOfPath = new RobotGrid(10.1, 23.3, 0, 20);

the Most commonly used method in the pathing code is the addPoint method it had 3 parameters
the first being the distance between the front of the robot and the back wall that you want the robot to end at
the second being the distance between the middle of the robot and left wall that you want the robot to end at 
the third being the angle you want the robot to end at 
there are other methods for creating arcs however and these can not get you everything this only can produce arcs with 90 degrees of angle change or less

If you need to drive and keep the robot facing the same way but need to go forward and to the left or right there is the s curve method with 2 parameters
The first one being the distance from the back wall that you want the robot to end at
The second it the distance from the left wall to the middle of the robot where you want the robot to end 

as a general principle think of the field as a coordinate plane with the x axis being the left side wall 
