package power.hawks.frc.lib.sens;

import edu.wpi.first.wpilibj.SerialPort;
import power.hawks.frc.lib.subsys.DriveTrain;

/**
 * The PixyCam Sensor used on the robot
 * <br>Note: The PixyCam was never implemented or used however, this code was tested with moderate success. Recommend using this as a reference only. </br>
 * @author Power Hawks Controls
 *
 */
public class PixyCam {
	SerialPort Serial = new SerialPort(9600, SerialPort.Port.kUSB1);
	DriveTrain driveTrain;
	
	/**
	 * Create a PixyCam subsystem that can interact with the drive train and maneuver the robot to center on an object
	 * @param dt the drive train of the robot
	 */
	public PixyCam(DriveTrain dt) {
		driveTrain = dt;
	}
	
	/**
	 * Centered the robot on an object as it was detected by the PixyCam
	 * <br>Note: this was run only experimentally and mainly just moved the robot back and forth based on the proximity of the detected object</br>
	 */
	public void pixy() {
		try {
			String pixy_s = Serial.readString().trim();
			if (pixy_s.length() > 0) {
				System.out.println(pixy_s); //Debug
				Integer x = Integer.parseInt(pixy_s);
				if (x > 180) { //Drive FORWARDS if the object is too far away
					driveTrain.drive(.15);

				} 
				else if (x < 140) { //Drive BACKWARDS if the object is too close
					driveTrain.drive(-.15);
				} 
				else if (x > 140 && x < 180) { //STOP if the object is in the right range
					System.out.println("stop");
					driveTrain.drive(0);
				}

			}
		} 
		catch (Exception e) {
			System.out.println("error");
		}
	}
}