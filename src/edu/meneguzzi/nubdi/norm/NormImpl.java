/**
 * 
 */
package edu.meneguzzi.nubdi.norm;

import jason.asSemantics.Unifier;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Term;
import edu.meneguzzi.nubdi.agent.nu.NuAgent;

/**
 * @author meneguzzi
 *
 */
public class NormImpl implements Norm {
	
	protected NormType normType;
	
	protected String targetAgent = null;
	
	protected String targetRole = null;
	
	protected Term normTarget;
	
	protected LogicalFormula normRestriction;
	
	protected boolean activated;
	
	protected LogicalFormula activationCondition;
	
	protected LogicalFormula expirationCondition;
	
	protected String normId;
	
	protected Unifier unifier = null;
	
	/**
	 * Creates a norm from a term encoding it
	 * @param norm
	 */
	public NormImpl(Term norm) {
		
	}
	
	public NormImpl(NormType normType, String targetAgent, String targetRole, 
			        Term normTarget, LogicalFormula normRestriction,
			        LogicalFormula activationCondition, LogicalFormula expirationCondition,
			        String normId) {
		this.normType = normType;
		this.targetAgent = targetAgent;
		this.targetRole = targetRole;
		this.normTarget = normTarget.clone();
		this.normRestriction = (LogicalFormula) normRestriction.clone();
		this.activationCondition = (LogicalFormula) activationCondition.clone();
		this.expirationCondition = (LogicalFormula) expirationCondition.clone();
		this.normId = normId;
		activated = false;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#getNormId()
	 */
	@Override
	public String getNormId() {
		return normId;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#getNormRestriction()
	 */
	@Override
	public LogicalFormula getNormRestriction() {
		return normRestriction;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#getNormTarget()
	 */
	@Override
	public Term getNormTarget() {
		return normTarget;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#getNormType()
	 */
	@Override
	public NormType getNormType() {
		return normType;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#getTargetAgent()
	 */
	@Override
	public String getTargetAgent() {
		return targetAgent;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#getTargetRole()
	 */
	@Override
	public String getTargetRole() {
		return targetRole;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#getUnifier()
	 */
	@Override
	public Unifier getUnifier() {
		return this.unifier;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#instantiateNorm(jason.asSemantics.Unifier)
	 */
	@Override
	public Norm instantiateNorm(Unifier unifier) {
		NormImpl norm = new NormImpl(normType, targetAgent, targetRole, normTarget, 
				              normRestriction, activationCondition, 
				              expirationCondition, normId+unifier.toString());
		//TODO make sure the unifier application occurs well
		norm.applyUnifier(unifier);
		return norm;
	}
	
	protected boolean applyUnifier(Unifier unifier) {
		this.unifier = unifier;
		if(!normTarget.apply(unifier)) {
			return false;
		}
		if(!normRestriction.apply(unifier)) {
			return false;
		}
		if(!activationCondition.apply(unifier)) {
			return false;
		}
		if(!expirationCondition.apply(unifier)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#isActivated()
	 */
	@Override
	public boolean isActivated() {
		return activated;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#supportsActivation(edu.meneguzzi.nubdi.agent.nu.NuAgent)
	 */
	@Override
	public boolean supportsActivation(NuAgent agent) {
		//TODO Make instances of each possible unifier for a certain norm?
		return activationCondition.logicalConsequence(agent, unifier) != null;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#supportsExpiration(edu.meneguzzi.nubdi.agent.nu.NuAgent)
	 */
	@Override
	public boolean supportsExpiration(NuAgent agent) {
		return expirationCondition.logicalConsequence(agent, unifier) != null;
	}

}
