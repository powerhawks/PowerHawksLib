package power.hawks.frc.lib.auto.cmds;

import power.hawks.frc.lib.Utility;
import power.hawks.frc.lib.auto.cmds.Command;
import power.hawks.frc.lib.sens.Ultrasonic;
import power.hawks.frc.lib.subsys.DriveTrain;

/**
 * Command that checks the robot's position based on the ultrasonic sensors mounted on-board.
 * <br> Note: this was never implemented or tested as there wasn't enough time so this command should only be used for reference. </br>
 * @author Power Hawks Controls
 *
 */
public class CheckCommand implements Command {
	DriveTrain driveTrain;
	Ultrasonic[] ultrasonics;
	double xPos;
	double xDZ;
	double yPos;
	double yDZ;
	boolean complete = false;
	
	/**
	 * Constructor for the checking command that defined the x/y bounds of the area the robot should be in
	 * @param dt the drive train of the robot
	 * @param x the specific x-coordinate the robot should be at
	 * @param dx the x-bound of the area the robot should be in
	 * @param y the specific y-coordinate the robot should be at
	 * @param dy the y-bound of the area the robot should be in
	 * @param u the array of ultrasonic sensors on-board
	 */
	public CheckCommand(DriveTrain dt, double x, double dx, double y, double dy, Ultrasonic[] u) {
		driveTrain = dt;
		xPos = x;
		xDZ = dx;
		yPos = y;
		yDZ = dy;
		ultrasonics = u;
	}
	
	public void execute() {
		if (Utility.inRange(ultrasonics[0].getDistance(), xPos, xDZ)) {
			if (Utility.inRange(ultrasonics[1].getDistance(), yPos, yDZ)) {
				complete = true;
			}
			else {
				complete = compensate(1, ultrasonics[1].getDistance());
			}
		}
		else {
			complete = compensate(0, ultrasonics[0].getDistance());
		}
	}
	
	private boolean compensate(int axis, double curReading) {
		double error;
		if (axis == 0) { //X-Axis compensation
			error = xPos - curReading;
			driveTrain.turnTo(0);
			if (!driveTrain.isTurning()) {
				return false;
			}
			else if (!driveTrain.isDriving()) {
				driveTrain.driveDistance(error, 0);
				return false;
			}
			else {
				return true;
			}
		}
		else { //Y-Axis compensation
			error = yPos - curReading;
			driveTrain.driveDistance(error, 90);
			return driveTrain.isDriving();
		}
	}

	@Override
	public boolean isComplete() {
		return complete;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
