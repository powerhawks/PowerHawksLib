package power.hawks.frc.lib;

/**
 * Basic class that has simple wrappers for quality-of-life things
 * @author Power Hawks Controls
 *
 */
public class Utility {
	/**
	 * Wrapper for checking if an input is in range of the target and the bounds
	 * @param x the reference number
	 * @param target the target
	 * @param bound the bounds (deadzone) around the target
	 * @return if x is in range of the target
	 */
	public static boolean inRange(double x, double target, double bound) {
		return x < target + bound && x > target - bound;
	}
	
	/**
	 * Wrapper for configuring the PID of a controller
	 * @param p the P-Gain
	 * @param i the I-Gain
	 * @param d the D-Gain
	 * @param pid the PID controller
	 */
	public static void configurePID(double p, double i, double d, MiniPID pid) {
		pid.setP(p); pid.setI(i); pid.setD(d);
	}
}
