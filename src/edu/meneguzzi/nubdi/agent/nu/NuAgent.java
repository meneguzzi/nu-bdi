/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu;

import jason.asSyntax.Term;

import java.util.Hashtable;
import java.util.List;

import edu.meneguzzi.nubdi.agent.ModularAgent;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultActionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultBeliefRevisionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultEventSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultIntentionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultMessageSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultOptionSelectionFunction;
import edu.meneguzzi.nubdi.agent.nu.function.NuBeliefUpdateFunction;
import edu.meneguzzi.nubdi.norm.Norm;

/**
 * @author meneguzzi
 * 
 */
public class NuAgent extends ModularAgent {
	
	protected Hashtable<String, Norm> abstractNorms;
	
	protected Hashtable<String, Norm> specificNorms;
	
	/**
	 * 
	 */
	public NuAgent() {
		this.actionSelectionFunction = new DefaultActionSelectionFunction();
		this.beliefRevisionFunction = new DefaultBeliefRevisionFunction();
		this.beliefUpdateFunction = new NuBeliefUpdateFunction();
		this.eventSelectionFunction = new DefaultEventSelectionFunction();
		this.intentionSelectionFunction = new DefaultIntentionSelectionFunction();
		this.messageSelectionFunction = new DefaultMessageSelectionFunction();
		this.optionSelectionFunction = new DefaultOptionSelectionFunction();
		
		
	}
	
	/**
	 * Updates the set of norms affecting this agent
	 * @param norms
	 * @return
	 */
	public boolean updateNorms(List<Term> norms) {
		return false;
	}
	
	public void resolveNormConflicts() {
		
	}
	
	public void annotatePlans() {
		
	}
}
