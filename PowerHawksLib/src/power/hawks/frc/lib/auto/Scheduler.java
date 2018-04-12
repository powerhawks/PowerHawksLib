package power.hawks.frc.lib.auto;



import java.util.ArrayList;

import power.hawks.frc.lib.auto.cmds.Command;

/**
 * A scheduler for executing commands in Autonomous mode
 * @author Power Hawks Controls
 *
 */
public class Scheduler {
	//Flags
	boolean done = false;
	boolean running = false;
	
	//Command list
	int i = 0;
	ArrayList<Command> commands;
	
	/**
	 * Generic constructor for Scheduler
	 */
	public Scheduler() {
	}
	
	/**
	 * Constructor for scheduler that takes in an ArrayList of commands
	 * @param coms and ArrayList of commands to execute
	 */
	public Scheduler(ArrayList<Command> coms) {
		commands = coms;
	}
	
	/**
	 * Runs the commands that are generated from generatePath().
	 */
	public void run() {
		Command com = commands.get(i);
		
		if (!done) {
			com.execute();
		}
		
		if (!com.isComplete()) {
			running = true;
		} 
		else {
			com.stop();
			
			if (i < commands.size()-1) {
				i++;
			}
			else {
				done = true;
				running = false;
			}
		}
	}
	
	/**
	 * Method that adds commands to the scheduler to be executed
	 * @param coms the commands to be executed
	 */
	public void addCommands(ArrayList<Command> coms) {
		commands = coms;
	}
	
	/**
	 * Wrapper that resets all flags and clears any commands out of the scheduler
	 */
	public void reset() {
		commands.clear();
		done = false;
		running = false;
		i = 0;
	}
}
