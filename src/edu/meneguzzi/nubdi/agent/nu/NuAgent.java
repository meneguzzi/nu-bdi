/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu;

import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Plan;
import jason.asSyntax.PlanBody;
import jason.asSyntax.Term;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import edu.meneguzzi.nubdi.agent.ModularAgent;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultActionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultBeliefRevisionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultEventSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultIntentionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultMessageSelectionFunction;
import edu.meneguzzi.nubdi.agent.nu.function.NuBeliefUpdateFunction;
import edu.meneguzzi.nubdi.agent.nu.function.NuOptionSelectionFunction;
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
		this.beliefUpdateFunction =  new NuBeliefUpdateFunction();
		this.eventSelectionFunction = new DefaultEventSelectionFunction();
		this.intentionSelectionFunction = new DefaultIntentionSelectionFunction();
		this.messageSelectionFunction = new DefaultMessageSelectionFunction();
		//this.optionSelectionFunction = new DefaultOptionSelectionFunction();
		this.optionSelectionFunction = new NuOptionSelectionFunction();
		
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
	 * Returns a collection of the abstract norms, only for testing purposes 
	 * hence it is package public
	 * @return
	 */
	Collection<Norm> getAbstractNorms() {
		return this.abstractNorms.values();
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
			//Should we annotate plans all over again at each cycle?
			//this.annotatePlans();
			return true;
		} else {
			logger.fine("Tried to add a specific norm that is already in the table of specific norms.");
			return false;
		}
	}
	
	/**
	 * Returns a collection of the  specific norms, only for testing purposes 
	 * hence it is package public
	 * @return
	 */
	Collection<Norm> getSpecificNorms() {
		return this.specificNorms.values();
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
		logger.fine("Updating norms");
		boolean normsChanged = false;
		Iterator<Unifier> activated = null;
		for(Norm abstractNorm : abstractNorms.values()) {
			logger.fine("Checking norm: "+abstractNorm);
			if((activated=abstractNorm.supportsActivation(this))!=null) {
				//logger.info("Norm has been activated");
				while(activated.hasNext()) {
					Unifier un = activated.next();
					Norm specificNorm = abstractNorm.instantiateNorm(un);
					if(this.addSpecificNorm(specificNorm)) {
						logger.info("Norm has been activated with unifier "+un);
						normsChanged |= true;
					}
				}
			}
		}
		for(Norm specificNorm : specificNorms.values()) {
			if(specificNorm.supportsExpiration(this)) {
				logger.info("Norm "+specificNorm+" has expired, removing from specific norms.");
				this.removeSpecificNorm(specificNorm.getNormId());
				normsChanged |= true;
			}
		}
		if(normsChanged) {
			this.annotatePlans();
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
		ConstraintAnnotation annotation = null;
		for(PlanBody step = plan.getBody(); step!=null; step=step.getBodyNext()) {
			logger.finest("Annotating step "+step);
			for(Norm sNorm : specificNorms.values()) {
				ConstraintAnnotation newAnnotation = null;
				//TODO Check if we really want this to be Literal, if exceptions occur, maybe we are talking about structure or term
				if(step.getBodyTerm()!=null && sNorm.inScope(this, null, (Literal)step.getBodyTerm())) {
					//TODO Make sure we won't need the unifier we got from 
					//TODO unifying the step with the norm for the norm restriction
					logger.info("Norm "+sNorm+" is in scope of step "+step+" with restriction "+sNorm.getNormRestriction());
					newAnnotation = new ConstraintAnnotation(sNorm.getNormRestriction());
					//If we are talking about prohibition, we need to inverse them
					if(sNorm.getNormType() == NormType.prohibition) {
						newAnnotation = newAnnotation.negate();
					}
				} else {
					newAnnotation = new ConstraintAnnotation();
				}
				//If we are in the first step of a plan our entire annotation 
				//will be the annotation for the current step
				if(annotation == null) {
					annotation = newAnnotation;
				} else {
					annotation.compose(newAnnotation);
				}
			}
		}
		String planLabel = plan.getLabel().toString();
		if(annotation != null) {
			this.planAnnotations.put(planLabel, annotation);
		} else {
			this.planAnnotations.remove(planLabel);
		}
	}
	
	/**
	 * Returns the {@link ConstraintAnnotation} for the referred plan indexed
	 * by its label.
	 * 
	 * @param planLabel
	 * @return
	 */
	public ConstraintAnnotation getAnnotationForPlan(String planLabel) {
		return this.planAnnotations.get(planLabel);
	}
}
