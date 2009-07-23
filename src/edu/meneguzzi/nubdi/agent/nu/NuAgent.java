/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu;

import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Plan;
import jason.asSyntax.PlanBody;
import jason.asSyntax.Term;

import java.util.Hashtable;
import java.util.Iterator;

import edu.meneguzzi.nubdi.agent.ModularAgent;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultActionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultBeliefRevisionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultBeliefUpdateFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultEventSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultIntentionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultMessageSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultOptionSelectionFunction;
import edu.meneguzzi.nubdi.exception.NuBDIException;
import edu.meneguzzi.nubdi.norm.Norm;
import edu.meneguzzi.nubdi.norm.NormImpl;
import edu.meneguzzi.nubdi.norm.Norm.NormType;

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
		
		this.abstractNorms = new Hashtable<String, Norm>();
		this.specificNorms = new Hashtable<String, Norm>();
		this.planAnnotations = new Hashtable<String, ConstraintAnnotation>();
	}
	
	/**
	 * Adds a new (abstract) norm to the agent, if the norm already exists in
	 * the agent, then this method returns false, indicating some problem with
	 * the normative processing
	 * @param term
	 * @return
	 */
	public boolean addAbstractNorm(Term term) throws NuBDIException {
		//First we need to make sure that the new norm is valid
		Norm newNorm;
		newNorm = new NormImpl(term);
		
		//Then we need to check if the given norm is in conflict with any 
		//other norm
		for(Norm specNorm : abstractNorms.values()) {
			if(newNorm.inConflict(specNorm)) {
				//TODO Check that the norm resolution will not break this norm 
				//down into multiple ones
				resolveNormConflict(newNorm, specNorm);
			}
			//Maybe we should set some flag for the above norm condition
			//To indicate that we should not simply add the norm below
		}
		
		if(abstractNorms.get(newNorm.getNormId())==null) {
			abstractNorms.put(newNorm.getNormId(), newNorm);
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
		
		if(specificNorms.get(newNorm.getNormId()) == null) {
			specificNorms.put(newNorm.getNormId(), newNorm);
			this.annotatePlans();
			return true;
		} else {
			logger.severe("Tried to add a specific norm that is already in the table of specific norms.");
			return false;
		}
	}
	
	/**
	 * Removes an abstract norm from an agent, presumably because the agent
	 * decided against complying with the norm, or because some authority has
	 * retracted the norm.
	 * 
	 * TODO Decide whether instances of this norm will be removed as well.
	 * 
	 * @param normId
	 * @return
	 */
	public Norm removeAbstractNorm(String normId) {
		//TODO Do any further processing of norms within the agent after this
		return abstractNorms.remove(normId);
	}
	
	/**
	 * Removes the specified specific norm, presumably because it has expired.
	 * This method removes the norm, as well as any annotations associated with 
	 * the specific norm from the plans and (maybe) intentions.
	 * 
	 * TODO Decide whether or not intentions will also be purged from annotations
	 * TODO after norms are removed
	 * 
	 * @param normId
	 * @return the norm that has just been removed
	 */
	public Norm removeSpecificNorm(String normId) {
		//TODO Do any further processing of the norms within the agent after this
		return specificNorms.remove(normId);
	}
	
	/**
	 * Updates the set of norms affecting this agent
	 * @param norms
	 * @return
	 */
	public boolean updateNorms() {
		//Add newly activated norms
		Iterator<Unifier> activated = null;
		for(Norm abstractNorm : abstractNorms.values()) {
			if((activated=abstractNorm.supportsActivation(this))!=null) {
				while(activated.hasNext()) {
					Unifier un = activated.next();
					Norm specificNorm = abstractNorm.instantiateNorm(un);
					//TODO review this algorithm
				}
			}
		}
		for(Norm specificNorm : specificNorms.values()) {
			if(specificNorm.supportsExpiration(this)) {
				this.removeAbstractNorm(specificNorm.getNormId());
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
	
	/**
	 * Maybe not the best algorithm for plan annotation, since we regenerate annotations
	 * at every step
	 */
	public void annotatePlans() {
		for(Plan plan : getPL()) {
			this.annotatePlan(plan);
		}
	}
	
	/**
	 * Annotates a plan according to algorithm 8 (so far) in the paper
	 * @param plan
	 */
	public void annotatePlan(Plan plan) {
		ConstraintAnnotation annotation = new ConstraintAnnotation();
		for(PlanBody step = plan.getBody(); step.getBodyNext()!=null; step=step.getBodyNext()) {
			for(Norm sNorm : specificNorms.values()) {
				ConstraintAnnotation newAnnotation = null;
				//TODO Check if we really want this to be Literal, if exceptions occur, maybe we are talking about structure or term
				if(sNorm.inScope(this.getTS().getUserAgArch().getAgName(), null, (Literal)step.getBodyTerm())) {
					newAnnotation = new ConstraintAnnotation(sNorm.getNormRestriction());
					//If we are talking about prohibition, we need to inverse them
					if(sNorm.getNormType() == NormType.prohibition) {
						newAnnotation = newAnnotation.negate();
					}
				} else {
					newAnnotation = new ConstraintAnnotation();
				}
				annotation.compose(newAnnotation);
			}
		}
		this.planAnnotations.put(plan.getLabel().toString(), annotation);
	}
}
