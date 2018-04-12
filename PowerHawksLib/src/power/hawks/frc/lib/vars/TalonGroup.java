package power.hawks.frc.lib.vars;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import power.hawks.frc.lib.subsys.DriveTrain;

/**
 * Class used for grouping TalonSRXs together for easier code flow
 * @author Power Hawks Controls
 *
 */
public class TalonGroup {
	TalonSRX[] controllers;
	TalonSRX driveEncoder;
	
	/**
	 * Creates a group of TalonSRXs from a list of controllers
	 * @param cons the list of TalonSRX controllers
	 * @param i the index of controller with encoder feedback
	 */
	public TalonGroup(TalonSRX[] cons, int i) {
		controllers = cons;
		driveEncoder = controllers[i];
	}
	
	/**
	 * Sets the power of all of the Talons
	 * @param power the power of the Talons
	 */
	public void set(double power) {
		for (TalonSRX t:controllers) {
			t.set(ControlMode.PercentOutput, power);
		}
	}
	
	/**
	 * Configures the Talon indicated by the passed index with an absolute encoder
	 */
	public void configFeedbackSensor() {
		driveEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, DriveTrain.TIMEOUT);
	}
	
	/**
	 * Gets the data from the encoder mounted to the Talon indicated by the passed index
	 * @return encoder ticks from Talon
	 */
	public int getSensorData() {
		return driveEncoder.getSelectedSensorPosition(0);
	}
	
	/**
	 * Inverts all of the Talons in the group
	 * @param inv whether the Talons are inverted or not
	 */
	public void setInverted(boolean inv) {
		for (TalonSRX t:controllers) {
			t.setInverted(inv);
		}
	}
	
	/**
	 * Resets the encoder position on the Talon indicated by the passed index to 0
	 */
	public void resetEncoder() {
		for (TalonSRX t:controllers) {
			t.setSelectedSensorPosition(0, 0, DriveTrain.TIMEOUT);
		}
	}
}
