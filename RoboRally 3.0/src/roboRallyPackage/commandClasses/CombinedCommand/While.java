
package roboRallyPackage.commandClasses.CombinedCommand;

import roboRallyPackage.gameElementClasses.*;
import roboRallyPackage.commandClasses.*;

/**
 * @version   24 may 2012
 * @author	  Jonas Schouterden (r0260385) & Nele Rober (r0262954)
 * 			  Bachelor Ingenieurswetenschappen, KULeuven
 */
public class While extends CombinedCommand
{
	public While(Robot robot, Condition condition, Command whileCommand)
	{
		super(robot);
		this.condition = condition;
		this.whileCommand = whileCommand;
	}
	
	Condition condition;

	public Condition getCondition()
	{
		return this.condition;
	}

	public Command getWhileCommand()
	{
		return this.whileCommand;
	}

	Command whileCommand;
	
	public void execute()
	{
		while(this.getCondition().results())
		{
			this.getWhileCommand().execute();
		}
	}
	
	public void executeStep()
	{
		if(this.getCondition().results())
		{
			this.getWhileCommand().executeStep();
		}
	}
	
	@Override
	public String toString()
	{
		return "(while" + "\n" + "  " + this.getCondition().toString()
				+ "\n" + "  " + this.getWhileCommand().toString() 
				+ "\n" +")";
	}
}
