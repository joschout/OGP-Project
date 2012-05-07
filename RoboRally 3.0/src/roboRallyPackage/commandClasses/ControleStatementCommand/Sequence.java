/**
 * 
 */
package roboRallyPackage.commandClasses.ControleStatementCommand;

import roboRallyPackage.*;
import roboRallyPackage.gameElementClasses.*;
import roboRallyPackage.commandClasses.*;
/**
 * @author Nele
 *
 */
public class Sequence extends ControleStatementCommand
{
	public Sequence(Robot robot, java.util.List<Executable> seqCommands)
	{
		super(robot);
		this.seqCommands = new java.util.ArrayList<Executable>(seqCommands);
	}
	
	java.util.List<Executable> seqCommands;

	/**
	 * @return	...
	 *			| result == ...
	 */
	public java.util.List<Executable> getSeqCommands() {
		return this.seqCommands;
	}
	
	public void execute()
	{
		for(Executable command: seqCommands)
		{
			command.execute();
		}
	}
}
