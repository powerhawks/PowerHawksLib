package power.hawks.frc.lib.auto.cmds;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import power.hawks.frc.lib.auto.cmds.Command;

/**
 * Class that allows multiple commands to be executed in parallel
 * @author Braidan
 *
 */
public class CommandGroup implements Command {
	//Commands
	ArrayList<Command> commands;
	
	//Parallel Execution
	ArrayList<Double> delays;
	Timer timer = new Timer();
	int i = 0;
	
	//Flags
	boolean timing = false;
	
	
	/**
	 * The CommandGroup class binds together multiple commands and executes, stops, and reports their completion in parallel.
	 * Note: The same subsystem cannot be used in parallel
	 * @param coms the ArrayList of commands that will be bound together
	 */
	public CommandGroup(ArrayList<Command> coms) {
		commands = coms;
	}
	
	/**
	 * <p> The CommandGroup class binds together multiple commands and executes, stops, and reports their completion in parallel.
	 * This Constructor uses delays between commands being executed in parallel. 
	 * </p>
	 * <p>Sequence the commands in the order you want them to be executed and match the contents of the delays accordingly
	 * i.e. [aCommand, bCommand, cCommand] and [0, .375] will execute aCommand and bCommand simultaneously and wait .375 seconds to execute cCommand.
	 * <br>[aCommand, cCommand, bCommand] and [.375, 0] will execute aCommand, wait .375 seconds, then execute cCommand and bCommand simultaneously.
	 * </p>
	 * Note: The same subsystem cannot be used in parallel
	 * @param coms the ArrayList of commands that will be bound together
	 * @param d a list of delays that will be between subsequent commands
	 */
	public CommandGroup(ArrayList<Command> coms, ArrayList<Double> d) {
		commands = coms;
		delays = d;
	}
	
	/**
	 * Execute the commands
	 */
	@Override
	public void execute() {
		if (delays == null) {
			executeNormal();
		}
		else {
			executeDelay();
		}
	}
	
	private void executeNormal() {
		for (Command c:commands) {
			c.execute();
		}
	}
	
	private void executeDelay() {
		double delay = delays.get(i);
		
		if (!timing) {
			timer.start();
			timing = true;
		}
		
		if (timer.hasPeriodPassed(delay)) {
			i++;
		}
		
		for (int x = 0; x <= i; x++) {
			commands.get(x).execute();
		}
	}
	
	/**
	 * Stop the commands
	 */
	@Override
	public void stop() {
		for (Command c:commands) {
			c.stop();
		}
	}

	/**
	 * Reports the completion of all commands in the group
	 */
	@Override
	public boolean isComplete() {		
		for (Command c:commands) {
			if (!c.isComplete()) {
				return false;
			}
		}
		
		return true;
	}

}
