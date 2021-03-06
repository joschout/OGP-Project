
package roboRallyPackage.gameElementClasses;
import roboRallyPackage.Board;
import roboRallyPackage.Position;
import be.kuleuven.cs.som.annotate.*;

/**
 * A class of repair kits. Extends Item.
 * 
 * @version   24 may 2012
 * @author	  Jonas Schouterden (r0260385) & Nele Rober (r0262954)
 * 			  Bachelor Ingenieurswetenschappen, KULeuven
 */
public class RepairKit extends Item implements IEnergyHolder
{
	/**
	 * Initialize this new repair kit with the given position, board, amount of energy [Ws] and weight [g].
	 * 
	 * @param 	position
	 * 			The position for this new element.
	 * @param	board
	 * 			The board on which this new element will be placed.
	 * @param 	energyAmount
	 * 			The amount of energy for this new repair kit, expressed in watt-seconds [Ws].
	 * @param 	weight
	 * 			The weight for this new repair kit, expressed in grams [g]. 
	 * @effect	This new repair kit is initialized as an Item with the given position, board and weight.
	 * 			| super(position, board, weight)
	 * @effect	...
	 * 			| if this.canHaveAsEnergy(energyAmount) then this.setEnergy(energyAmount)
	 */
	public RepairKit(Position position, Board board, double energyAmount, int weight)
	{
		super(position, board, weight);
		if(this.canHaveAsEnergy(energyAmount))
		{
			this.setEnergy(energyAmount);
		}
	}
	
	/**
	 * Initialize this new repair kit with the given amount of energy [Ws] and weight [g].
	 * 
	 * @param 	energyAmount
	 * 			The amount of energy for this new repair kit, expressed in watt-seconds [Ws].
	 * @param 	weight
	 * 			The weight for this new repair kit, expressed in grams [g].
	 * @pre		The given initial amount of energy must be a valid amount of energy.
	 * 			| this.canHaveAsEnergy(energyAmount)
	 * @effect	...
	 * 			| this(null, null, energyAmount, weight)
	 */
	public RepairKit(double energyAmount, int weight)
	{
		this(null, null, energyAmount, weight);
	}
	
	/**
	 * Returns the variable representing the current amount if energy of this battery in the given energy unit.
	 */
	@Basic @Override
	public double getEnergy(EnergyUnit unit)
	{
		return energyAmount.toEnergyUnit(unit).getAmount();
	}

	/**
	 * Sets the amount of energy of this repair kit to the given amount of energy, expressed in watt-seconds [Ws].
	 */
	@Override
	public void setEnergy(double energyAmount) throws IllegalStateException
	{
		assert this.canHaveAsEnergy(energyAmount): "The given amount of energy is not a valid amount of energy for this repair kit.";
		
		if(this.isTerminated())
		{
			throw new IllegalStateException("The energy of a terminated repair kit cannot be altered.");
		}
		
		this.energyAmount = new EnergyAmount(energyAmount, EnergyUnit.WATTSECOND);
		
	}
	
	/**
	 * Checks whether the given amount of energy, expressed in Watt-second [Ws], is a valid amount of energy for this repair kit.
	 */
	@Override
	public boolean canHaveAsEnergy(double energy)
	{
		return ((energy <= this.getMaxEnergy()) && EnergyAmount.isValidEnergyAmount(energy));
	}

	/**
	 * Variable representing the current amount if energy for this repair kit, expressed in watt-seconds [Ws].
	 */
	private EnergyAmount energyAmount = EnergyAmount.WATTSECOND_0;
	

	/**
	 * Returns the variable representing the maximum amount of energy this repair kit can have, expressed in watt-seconds [Ws].
	 */
	@Basic @Immutable @Override
	public final double getMaxEnergy()
	{
		return maxEnergyAmount.getAmountInWattSecond();
	}

	/**
	 * Sets the maximum energy level of this repair kit to the given maximum energy level, expressed in watt-seconds [Ws].
	 */
	@Override
	public void setMaxEnergy(double maxEnergyAmount) throws IllegalStateException,
															UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * Check whether the given maximum energy, expressed in watt-second [Ws], is a valid maximum energy for this repair kit.
	 */
	@Override
	public boolean canHaveAsMaxEnergy(double maxEnergy)
	{
		return (EnergyAmount.isValidEnergyAmount(maxEnergy) && maxEnergy <= Double.MAX_VALUE);
	}

	/**
	 * Variable representing the maximum amount of energy this repair kit can have, expressed in watt-seconds [Ws].
	 */
	private static final EnergyAmount maxEnergyAmount = EnergyAmount.WATTSECOND_DOUBLE_MAXVALUE;

	/**
	 * Returns a percentage of the current energy level relative to the maximum amount of energy this repair kit can have.
	 */
	@Override
	public double getEnergyFraction()
	{
		return (this.getEnergy(EnergyUnit.WATTSECOND) / this.getMaxEnergy()) * 100;
	}

	/**
	 * Recharges this repair kit with the given amount of energy, expressed in Watt-second [Ws].
	 */
	@Override
	public void recharge(double amount) throws IllegalStateException
	{
		assert this.canAcceptForRecharge(amount): "The given amount is not a valid amount for this repair kit to be recharged with.";
		
		if(this.isTerminated())
		{
			throw new IllegalStateException("A terminated repairkit cannot be recharged.");
		}
		this.setEnergy(this.getEnergy(EnergyUnit.WATTSECOND) + amount);
	}

	/**
	 * Checks whether the given amount of energy, expressed in watt-seconds [Ws], can be added to the current energy level of this repair kit.
	 */
	@Override
	public boolean canAcceptForRecharge(double amount)
	{
		return (EnergyAmount.isValidEnergyAmount(amount) && this.canHaveAsEnergy(this.getEnergy(EnergyUnit.WATTSECOND) + amount));
	}

	/**
	 * This repair kit repairs another IEnergyHolder bay increasing its maximum energy.
	 * When the other IEnergyHolder cannot alter its maximum energy, nothing is changed.
	 * 
	 * @param	other
	 * 			The IEnergyHolder to be given energy.
	 * @param	amount
	 * 			The amount of energy to be transferred, expressed Watt-second [Ws].
	 * @pre		The amount to be transferred cannot be greater than the amount of energy this repair kit has.
	 * 			| amount <= this.getEnergy(EnergyUnit.WATTSECOND)
	 * @pre		The IEnergyHolder must be able to alter its maximum energy level with half of the given amount.
	 * 			| other.canHaveAsMaxEnergy(other.getMaxEnergy() + (amount/2))
	 * @effect	The maximum energy level of the other IEnergyHolder is increased with half of the given amount.
	 * 			| other.setMaxEnergy(other.getMaxEnergy() + (amount/2))
	 * @effect	The transferred amount of energy is subtracted from the amount of energy of this repair kit.
	 * 			| this.setEnergy(this.getEnergy() - amount)
	 * @throws	IllegalStateException
	 * 			...
	 * 			| this.isTerminated() || other.isTerminated()
	 */
	public void repair(IEnergyHolder other, double amount) throws IllegalStateException
	{
		assert(amount <= this.getEnergy(EnergyUnit.WATTSECOND)):"This repair kit cannot transfer more energy than it has.";
		assert(other.canHaveAsMaxEnergy(other.getMaxEnergy() + (amount/2))):"The other IEnergyHolder cannot increase its maximum energy level the energy to be transferred.";
		
		if(this.isTerminated() || other.isTerminated())
		{
			throw new IllegalStateException("Either this repair kit or the given IEnergyHolder is terminated. The IEnergyHolder cannot be repaired by the repair kit.");
		}
		
		try
		{
			other.setMaxEnergy(other.getMaxEnergy() + (amount)/2);
			this.setEnergy(this.getEnergy(EnergyUnit.WATTSECOND) - amount);
		}
		// the other IEnergyHolder cannot alter its maximum energy
		catch(UnsupportedOperationException exc)
		{
			System.err.println("The maximum energy level of the given IEnergyHolder cannot be altered.");
		}
	}
	
	/**
	 * Transfers the given amount of energy, expressed in watt-seconds [Ws], from this repair kit to another IEnergyHolder.
	 */
	@Override
	public void transferEnergy(IEnergyHolder other, double amount) throws IllegalStateException, UnsupportedOperationException
	{
		throw new UnsupportedOperationException();
	}

	
	/**
	 * The given robot uses this repair kit. As much energy as possible is transferred from this repair kit to the maximum energy level of the robot.
	 * 
	 * @param	robot
	 * 			The robot that uses this repair kit.
	 * @effect	...
	 * 			| if(robot.canHaveAsMaxEnergy(robot.getMaxEnergy() + (this.getEnergy(EnergyUnit.WATTSECOND)/2)))
	 * 			|	then this.repair(robot, this.getEnergy(EnergyUnit.WATTSECOND))
	 * 			| else this.repair(robot, robot.getMaxEnergy() - robot.getEnergy(EnergyUnit.WATTSECOND))
	 */
	@Override
	public void use(Robot robot) throws IllegalStateException
	{
		assert robot.getPossessions().contains(this): "The given robot is not carrying this item.";
		
		if(this.isTerminated())
		{
			throw new IllegalStateException("A terminated repairkit can not be altered.");
		}
		if(robot.isTerminated())
		{
			throw new IllegalStateException("A terminated robot can not be altered.");
		}
		
		// the robot has enough 'room' to store the total maximum energy level this repair kit holds.
		if(robot.canHaveAsMaxEnergy(robot.getMaxEnergy() + (this.getEnergy(EnergyUnit.WATTSECOND)/2)))
		{
			this.repair(robot, this.getEnergy(EnergyUnit.WATTSECOND));
			robot.getPossessions().remove(this);
			this.terminate();
		}
		
		// the robot can only store part of the energy this repair kit holds.
		else
		{
			this.repair(robot, robot.getMaxEnergy() - robot.getEnergy(EnergyUnit.WATTSECOND));
		}
	}
	
	/**
	 * When an element is hit (e.g. it is shot by a robot) some of its properties are altered.
	 * A repair kit increases its energy with 500 Ws when hit if possible. Otherwise it is set to the maximum energy level.
	 * 
	 * @post	The energy level of this repair kit is increaesed with 500 Ws if possible, otherwise it is set to the maximum energy level.
	 * 			| if(this.canHaveAsEnergy(this.getEnergy(EnergyUnit.WATTSECOND) + 500))
	 * 			|  then this.setEnergy(this.getEnergy(EnergyUnit.WATTSECOND) + 500)
	 * 			| else this.setEnergy(this.getMaxEnergy())
	 * @throws	IllegalStateException
	 * 			When this repair kit is terminated.
	 * 			| this.isTerminated()
	 */
	@Override
	public void takeHit() throws IllegalStateException
	{
		if(this.isTerminated())
		{
			throw new IllegalStateException("A terminated repair kit cannot be hit.");
		}
		
		// this repair kit has room for 500 Ws extra energy
		if(this.canHaveAsEnergy(this.getEnergy(EnergyUnit.WATTSECOND) + 500))
		{
			this.setEnergy(this.getEnergy(EnergyUnit.WATTSECOND) + 500);
		}
		// the energy level of this repair kit is almost at maximum; it cannot store another 500 Ws extra.
		// the energy level of this repair kit is set at its maximum.
		else
		{
			this.setEnergy(this.getMaxEnergy());
		}
	}
	
	/**
	 * Returns a string representation of this repair kit.
	 * 
	 * @return	...
	 * 			| result == "Repair kit with:" + "\n"
	 *			| 			+ super.toString() + "\n" 
	 *			| 			+ " energy level [Ws]: " + this.getEnergy(EnergyUnit.WATTSECOND) + "(" + getEnergyFraction() + "%)"
	 */
	@Override
	public java.lang.String toString()
	{
		return "Repairkit {"
				+ super.toString() +  ";  "
				+ "Energy level [Ws]: " + this.getEnergy(EnergyUnit.WATTSECOND) + " (" + this.getEnergyFraction() + "%)" + "}";
	}
}
