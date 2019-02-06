# FRCLib
These are the different library classes i have made over the years to acheive different controls tasks.
The encoder gyro class lets you get the angle of the robot without having a gyro as long as you have encoders on both sides of your drivetrain the param width is not the width of the robot but the inside to inside distance between the wheels so it can determine the turning radius

Followable is an interface that is required to use PathFollower

DriveStraight is a class called by PathFollower to make driving straight lines easier then using RobotGrid to path straight lines

RobotGrid is used to path complex curves used my PathFollower to make a robot follow the path

Position is a class used to store data for RobotGrid

PathFollower is a class that uses all of these classes together to follow paths
