package power.hawks.frc.lib.auto.cmds;

import power.hawks.frc.lib.auto.cmds.Command;
import power.hawks.frc.lib.subsys.DriveTrain;

/**
 * Command that turns the robot to a certain radial using the on-board NavX
 * @author Power Hawks Controls
 *
 */
public class TurnCommand implements Command {
	DriveTrain driveTrain;
	double target;
	boolean complete = false;
	
	/**
	 * Turns the robot to a certain radial
	 * @param dt the drive train subsystem
	 * @param t desired radial
	 */
	public TurnCommand(DriveTrain dt, double t) {
		driveTrain = dt;
		target = t;
	}

	public void execute() {
		driveTrain.turnTo(target);
		complete = !driveTrain.isTurning();
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
