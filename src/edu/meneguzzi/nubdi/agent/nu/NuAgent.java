/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu;

import jason.asSyntax.Term;

import java.util.Hashtable;

import edu.meneguzzi.nubdi.agent.ModularAgent;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultActionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultBeliefRevisionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultBeliefUpdateFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultEventSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultIntentionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultMessageSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultOptionSelectionFunction;
import edu.meneguzzi.nubdi.norm.Norm;
import edu.meneguzzi.nubdi.norm.NormImpl;

/**
 * @author meneguzzi
 * 
 */
public class NuAgent extends ModularAgent {
	
	protected Hashtable<String, Norm> abstractNorms;
	
	protected Hashtable<String, Norm> specificNorms;
	
	protected Hashtable<String, ConstraintAnnotation> planAnnotations;
	
	/**
	 * 
	 */
	public NuAgent() {
		this.actionSelectionFunction = new DefaultActionSelectionFunction();
		this.beliefRevisionFunction = new DefaultBeliefRevisionFunction();
		this.beliefUpdateFunction = new DefaultBeliefUpdateFunction();
		this.eventSelectionFunction = new DefaultEventSelectionFunction();
		this.intentionSelectionFunction = new DefaultIntentionSelectionFunction();
		this.messageSelectionFunction = new DefaultMessageSelectionFunction();
		this.optionSelectionFunction = new DefaultOptionSelectionFunction();
	}
	
	/**
	 * Adds a new (abstract) norm to the agent, if the norm already exists in
	 * the agent, then this method returns false, indicating some problem with
	 * the normative processing
	 * @param term
	 * @return
	 */
	public boolean addAbstractNorm(Term term) {
		Norm n = new NormImpl(term);
		
		if(abstractNorms.get(n.getNormId())==null) {
			abstractNorms.put(n.getNormId(), n);
			return true;
		} else {
			logger.severe("Tried to add an abstract norm that is already in the table of abstract norms.");
			return false;
		}
	}
	
	/**
	 * Adds a specific norm to the table of specific norms
	 * @param n
	 * @return
	 */
	public boolean addSpecificNorm(Norm newNorm) {
		//First we need to make sure that the new norm is actually activated
		//(Only activated norms can be specific norms)
		if(!newNorm.isActivated()) {
			return false;
		}
		//Then we need to check if the given norm is in conflict with any 
		//other norm
		for(Norm specNorm : specificNorms.values()) {
			if(newNorm.inConflict(specNorm)) {
				//TODO Check that the norm resolution will not break this norm 
				//down into multiple ones
				resolveNormConflict(newNorm, specNorm);
			}
			//Maybe we should set some flag for the above norm condition
			//To indicate that we should not simply add the norm below
		}
		
		if(specificNorms.get(newNorm.getNormId()) == null) {
			specificNorms.put(newNorm.getNormId(), newNorm);
			return true;
		} else {
			logger.severe("Tried to add a specific norm that is already in the table of specific norms.");
			return false;
		}
	}
	
	/**
	 * Updates the set of norms affecting this agent
	 * @param norms
	 * @return
	 */
	public boolean updateNorms() {
		for(Norm abstractNorm : abstractNorms.values()) {
			if(abstractNorm.isActivated()) {
				
			}
		}
		return true;
	}
	
	/**
	 * Resolves the conflict between two norms
	 * @param newNorm
	 * @param specNorm
	 */
	public void resolveNormConflict(Norm newNorm, Norm specNorm) {
		
	}
	
	public void annotatePlans() {
		
	}
}
