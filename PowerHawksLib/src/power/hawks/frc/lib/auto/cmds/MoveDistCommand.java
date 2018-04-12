package power.hawks.frc.lib.auto.cmds;

import power.hawks.frc.lib.auto.cmds.Command;
import power.hawks.frc.lib.subsys.DriveTrain;

/**
 * Command that moves the robot a certain distance
 * @author Power Hawks Controls
 *
 */
public class MoveDistCommand implements Command {
	DriveTrain driveTrain;
	double target;
	Double angle;
	boolean complete = false;
	
	/**
	 * Moves the robot a certain distance on a radial
	 * @param dt the drivetrain of the robot
	 * @param t the target distance
	 * @param a the radial angle (in degrees) the robot should travel on
	 */
	public MoveDistCommand(DriveTrain dt, double t, double a) {
		driveTrain = dt;
		target = t;
		angle = a;
	}
	
	public void execute() {
		driveTrain.driveDistance(target, angle);
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
