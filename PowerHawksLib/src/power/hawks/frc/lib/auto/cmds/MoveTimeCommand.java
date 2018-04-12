package power.hawks.frc.lib.auto.cmds;

import power.hawks.frc.lib.auto.cmds.Command;
import power.hawks.frc.lib.subsys.DriveTrain;

/**
 * Command for moving the robot for a certain time
 * @author Power Hawks Controls
 *
 */
public class MoveTimeCommand implements Command {
	DriveTrain driveTrain;
	double target;
	Double angle;
	boolean reverse;
	boolean complete = false;
	
	/**
	 * Moves the robot for a certain time on a certain radial
	 * @param dt the drive train of the robot
	 * @param t the time to drive for
	 * @param a the radial to travel on
	 * @param r reverse the drive train
	 */
	public MoveTimeCommand(DriveTrain dt, double t, double a, boolean r) {
		driveTrain = dt;
		target = t;
		angle = a;
		reverse = r;
	}
	
	@Override
	public void execute() {
		driveTrain.driveTime(target, angle, reverse);
		complete = !driveTrain.isDriving();
	}
	
	@Override
	public boolean isComplete() {
		return complete;
	}

	@Override
	public void stop() {
		driveTrain.stop();
	}
}
