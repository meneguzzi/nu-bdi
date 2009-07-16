/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu;

import edu.meneguzzi.nubdi.agent.ModularAgent;
import edu.meneguzzi.nubdi.agent.nu.function.NuBeliefUpdateFunction;

/**
 * @author meneguzzi
 * 
 */
public class NuAgent extends ModularAgent {
	
	
	/**
	 * 
	 */
	public NuAgent() {
		this.beliefUpdateFunction = new NuBeliefUpdateFunction();
	}
}
