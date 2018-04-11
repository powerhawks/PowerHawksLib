package power.hawks.frc.lib;

/**
 * Generic interface for autonomous commands
 * @author Power Hawks Controls
 *
 */
public interface Command {
	/**
	 * The desired target of a specific command i.e. angle for a TurnCommand
	 */
	public double target = 0;
	/**
	 * Flag if the command is complete
	 */
	public boolean complete = false;
	
	/**
	 * Execution method for the command
	 */
	public void execute();
	
	/**
	 * Stops the subsystem
	 */
	public void stop();
	
	/**
	 * Returns if the command is complete or not
	 */
	public boolean isComplete();
}
