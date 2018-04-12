package power.hawks.frc.lib.sens;

import java.util.Arrays;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * Custom class for ultrasonic sensors on the robot. 
 * The data is gathered over analog and therefore must be plugged into the Analog In ports on the RIO
 * @author Power Hawks Controls
 *
 */
public class Ultrasonic {
	AnalogInput input;
	double vScale = 5.0/512.0;
	
	/**
	 * Creates a new ultrasonic sensor in the specified port
	 * @param p the port the ultrasonic is plugged into.
	 */
	public Ultrasonic(int p) {
		input = new AnalogInput(p);
	}
	
	/**
	 * Pulls the data from the ultrasonic sensor and automatically converts it from a raw voltage to usable distance. Utilize getDistance()
	 * @return the distance reading from the sensor (in inches)
	 */
	public double getData() {
		return vScale / map(input.getValue(), 0, 4096, 0, 5);
	}
	
	/**
	 * Takes the median of three sensor readings to mostly filter out any echoes or noise. Utilize this over getData()
	 * @return the best, theoretically most accurate, distance reading (in inches)
	 */
	public double getDistance() {
		double[] data = new double[3];
		
		for (int x = 0; x < 3; x++) {
			data[x] = getData();
		}
		Arrays.sort(data);
		
		return data[1];
	}
	
	/**
	 * Maps an input from one range into another
	 * @param x the number to be converted
	 * @param inMin the minimum of the input range
	 * @param inMax the maximum of the input range
	 * @param outMin the minimum of the output range
	 * @param outMax the maximum of the output range
	 * @return the inputed number scaled to the new range
	 */
	private static double map(double x, double inMin, double inMax, double outMin, double outMax) {
		return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}
}
