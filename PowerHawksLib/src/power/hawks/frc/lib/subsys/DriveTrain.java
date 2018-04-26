 
package power.hawks.frc.lib.subsys;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import power.hawks.frc.lib.com.MiniPID;
import power.hawks.frc.lib.com.Utility;
import power.hawks.frc.lib.vars.TalonGroup;

/**
 * Basic drive train subsystem for the robot
 * @author Power Hawks Controls
 *
 */
public class DriveTrain {
	//Motors
	TalonGroup leftMotors;
	TalonGroup rightMotors;
	
	// NavX PID Variables
	final double pN = 0, iN = 0, dN = 0;
	AHRS navx = new AHRS(SPI.Port.kMXP);
	MiniPID pidNavx = new MiniPID(pN, iN, dN);

	// Encoder PID Variables
	double pT = 0, iT = 0, dT = 0;
	MiniPID pidDist = new MiniPID(pT, iT, dT);
	final double TPI = 1705;
	
	//Straight Driving Variables
	final double pS = 0, iS = 0, dS = 0;
	MiniPID pidStraight = new MiniPID(pS, iS, dS);
	
	// Timer
	Timer timer = new Timer();

	// Flags
	boolean low = false;
	boolean takeoff = false;
	boolean driving = false;
	boolean turning = false;
	boolean timing = false;

	// Standard Variables
	final double MAX_AUTO_SPEED = .6;
	@SuppressWarnings("javadoc")
	public final static int TIMEOUT = 200; //ms

	/**
	 * Initializes the drive train for use and configures PID for autonomous
	 */
	public DriveTrain(TalonGroup left, TalonGroup right, double[] dPV, double[] nPV, double[] sPV) {
		leftMotors = left;
		rightMotors = right;
		
		// Configure encoder PID
		leftMotors.configFeedbackSensor();
		rightMotors.configFeedbackSensor();
		leftMotors.resetEncoder();
		rightMotors.resetEncoder();
		Utility.configurePID(dPV, pidDist);
		pidDist.setOutputLimits(MAX_AUTO_SPEED);
		
		// Configure NavX PID
		navx.reset();
		Utility.configurePID(nPV, pidNavx);
		pidNavx.setOutputLimits(MAX_AUTO_SPEED);
		
		//Configure straight PID
		Utility.configurePID(sPV, pidStraight);
		pidStraight.setOutputLimits(MAX_AUTO_SPEED);
	}

	
	// =====STANDARD METHODS=====

	
	/**
	 * Drives the robot at the given power
	 * @param power the power of the motors
	 */
	public void drive(double power) {
		leftMotors.set(power);
		rightMotors.set(power);
	}
	
	/**
	 * Drives the robot in a tank drive/skidsteer configuration with left/right inputs
	 * @param left the power for the left motors
	 * @param right the power for the right motors
	 */
	public void drive(double left, double right) {
		leftMotors.set(left);
		rightMotors.set(right);
	}
	
	/**
	 * Drives the robot at the given power along a certain radial using the NavX
	 * <br> Note: whichever side is getting compensated power should be opposite of the encoder </br>
	 * @param power the power of the motors
	 * @param angle the radial to travel along
	 */
	public void driveRadial(double power, double angle) {
		pidStraight.setSetpoint(angle);
		double compensate = pidStraight.getOutput(Math.round(navx.getYaw()));
		 
		drive(power, power*(1-compensate));
	}

	
	// =====AUTO METHODS=====
	
	
	/**
	 * Uses PID, encoders, and the NavX to travel a certain distance along a certain radial and hold there
	 * @param dist the distance
	 * @param radial the radial to travel on
	 */
	public void driveDistance(double dist, double radial) {
		int deadzone = 1000;
		int desTicks = (int) (TPI * dist);
		pidDist.setSetpoint(desTicks);
		int curTicks = leftMotors.getSensorData();
		
		if (Utility.inRange(curTicks, desTicks, deadzone)) { // STOPS if in deadzone of distance
			drive(0);
			leftMotors.resetEncoder();
			rightMotors.resetEncoder();
			driving = false;
		}
		else { // DRIVES until traveled far enough to be in deadzone
			double speed = pidDist.getOutput(curTicks);
			driveRadial(-speed, radial);
			driving = true;
		}
		
		updateDashboard();
	}
	
	/**
	 * Uses a timer and the NavX to travel for a certain time along a certain radial
	 * @param time the time to drive
	 * @param radial the radial to travel on
	 * @param reverse if the drive train is reversed
	 */
	public void driveTime(double time, double radial, boolean reverse) {
		if (!timing) { // Start the timer and flip the TIMING flag
			timer.start();
			timing = true;
		}
		
		if (timer.get() < time) { // Run the motors at MAX AUTO SPEED for the specified time and flip the DRIVING flag
			if (reverse) {
				driveRadial(-MAX_AUTO_SPEED, radial);
			}
			else {
				driveRadial(MAX_AUTO_SPEED, radial); 
			}
			driving = true;
		}
		else { // Reset the timer and flip the TIMING and DRIVING flag
			timer.stop();
			timer.reset();
			timing = false;
			driving = false;
		}
		
		updateDashboard();
	}
	
	/**
	 * Uses the NavX and PID to turn to a specific angle
	 * @param desAngle the desired angle
	 */
	public void turnTo(double desAngle) {
		int deadzone = 2;
		int curAngle = Math.round(navx.getYaw());
		SmartDashboard.putNumber("Current Angle:", curAngle); // Debug
		pidNavx.setSetpoint(desAngle);
		double speed = pidNavx.getOutput(curAngle);

		if ((curAngle - desAngle < deadzone) && (curAngle - desAngle > -deadzone)) { //STOPS if in deadzone of angle
			drive(0, 0); // STOPS motors
			turning = false;
		} 
		else { //TURNS until facing a specified angle
			drive(-speed, speed);
			turning = true;
		}
		
		updateDashboard();
	}
	
	
	// =====UTILITY METHODS=====
	
	
	private void updateDashboard() {
		SmartDashboard.putBoolean("Driving:", driving);
		SmartDashboard.putBoolean("Turning:", turning);
		SmartDashboard.putBoolean("Timing:", timing);
	}
	
	/**
	 * Wrapper for stopping the drive train and reseting the drive encoders
	 */
	public void stop() {
		drive(0);
		leftMotors.resetEncoder();
		rightMotors.resetEncoder();
	}
	
	/**
	 * Wrapper for reseting the drive encoders and the NavX
	 */
	public void reset() {
		leftMotors.resetEncoder();
		rightMotors.resetEncoder();
		navx.reset();
	}
	
	
	//=====GETTERS AND SETTERS=====
	
	
	/**
	 * Getter for if the drive train is currently driving
	 * @return if the drive train is driving
	 */
	public boolean isDriving() {
		return driving;
	}
	
	/**
	 * Getter for if the drive train is currently turning
	 * @return if the drive train is turning
	 */
	public boolean isTurning() {
		return turning;
	}
}
