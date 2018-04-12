package power.hawks.frc.lib.vars;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import power.hawks.frc.lib.subsys.DriveTrain;

public class TalonGroup {
	TalonSRX[] controllers;
	TalonSRX driveEncoder;
	
	public TalonGroup(TalonSRX[] cons, int i) {
		controllers = cons;
		driveEncoder = controllers[i];
	}
	
	public void set(double power) {
		for (TalonSRX t:controllers) {
			t.set(ControlMode.PercentOutput, power);
		}
	}
	
	public void configFeedbackSensor() {
		driveEncoder.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, DriveTrain.TIMEOUT);
	}
	
	public int getSensorData() {
		return driveEncoder.getSelectedSensorPosition(0);
	}
	
	public void setInverted(boolean inv) {
		for (TalonSRX t:controllers) {
			t.setInverted(inv);
		}
	}
	
	public void resetEncoder() {
		for (TalonSRX t:controllers) {
			t.setSelectedSensorPosition(0, 0, DriveTrain.TIMEOUT);
		}
	}
}
