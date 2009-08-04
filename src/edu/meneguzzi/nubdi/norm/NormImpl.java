/**
 * 
 */
package edu.meneguzzi.nubdi.norm;

import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.StringTerm;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.asSyntax.parser.ParseException;

import java.util.Iterator;
import java.util.logging.Logger;

import edu.meneguzzi.nubdi.agent.nu.NuAgent;
import edu.meneguzzi.nubdi.exception.NuBDIException;

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
	public NormImpl(Term norm) throws NuBDIException {
		if(norm.isStructure()) {
			Structure sNorm = (Structure)norm;
			if(sNorm.getArity() < 6) {
				throw new NuBDIException("Norms must have at least 6 parameters: norm(Type, Norm, Restriction, Activation, Expiration, Identifier, <Agent>, <Role>)");
			}
			this.normType = parseNormType(sNorm.getTerm(0).toString());
			this.normTarget = sNorm.getTerm(1);
			try {
				this.normRestriction = termToLogicalFormula(sNorm.getTerm(2));
				this.activationCondition = termToLogicalFormula(sNorm.getTerm(3));
				this.expirationCondition = termToLogicalFormula(sNorm.getTerm(4));
			} catch (ClassCastException e) {
				throw new NuBDIException("Norm restriction, and activation and expiration conditions must be logcal formulas", e);
			} catch (ParseException e) {
				throw new NuBDIException("Norm restriction, and activation and expiration conditions must be logcal formulas", e);
			}
			this.normId = sNorm.getTerm(5).toString();
			this.activated = false;
			if(sNorm.getArity() > 6) {
				this.targetAgent = sNorm.getTerm(6).toString();
			}
			if(sNorm.getArity() > 7) {
				this.targetRole = sNorm.getTerm(7).toString();
			}
			this.unifier = new Unifier();
		} else {
			throw new NuBDIException("Norms must have at least 6 parameters: norm(Type, Norm, Restriction, Activation, Expiration, Identifier, <Agent>, <Role>)");
		}
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
		this.unifier = new Unifier();
	}
	
	public static LogicalFormula termToLogicalFormula(Term term) throws ParseException {
		LogicalFormula formula = null;
		if(term.isString()) {
			StringTerm sTerm = (StringTerm) term;
			formula = ASSyntax.parseFormula(sTerm.getString());
		} else {
			formula = ASSyntax.parseFormula(term.toString());
		}
		
		return formula;
	}
	
	/**
	 * Parses a specific NormType
	 * @param nType
	 * @return
	 * @throws NuBDIException
	 */
	public static NormType parseNormType(String nType) throws NuBDIException {
		if(nType.toString().equals("obligation")) {
			return NormType.obligation;
		} else if(nType.toString().equals("prohibition")) {
			return NormType.prohibition;
		} else {
			throw new NuBDIException("Invalid norm type (must be either obligation and prohibition): "+nType);
		}
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
		this.activated = true;
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
	public Iterator<Unifier> supportsActivation(NuAgent agent) {
		//TODO Make instances of each possible unifier for a certain norm?
		return activationCondition.logicalConsequence(agent, unifier);
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#supportsExpiration(edu.meneguzzi.nubdi.agent.nu.NuAgent)
	 */
	@Override
	public boolean supportsExpiration(NuAgent agent) {
		return expirationCondition.logicalConsequence(agent, unifier) != null;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#inScope(java.lang.String, java.lang.String, jason.asSyntax.Literal)
	 */
	@Override
	public boolean inScope(String agentId, String roleId, Literal literal) {
		//First check if the norm unifier matches the supplied literal
		literal = (Literal) literal.clone();
		if(literal.apply(this.unifier)) {
			//TODO double check this algorithm when the full implementation is done
		}
		
		Logger.getAnonymousLogger().severe("inScope not yet implemented");
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.norm.Norm#inConflict(edu.meneguzzi.nubdi.norm.Norm)
	 */
	@Override
	public boolean inConflict(Norm n) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("norm(");
		switch (normType) {
		case obligation:
			builder.append("obligation");
			break;
		case prohibition:
			builder.append("prohibition");
			break;
		default:
			break;
		}
		builder.append(targetAgent);
		builder.append(",");
		builder.append(normTarget);
		builder.append("*");
		builder.append(normRestriction);
		builder.append(",");
		builder.append(activationCondition);
		builder.append(",");
		builder.append(expirationCondition);
		builder.append(",");
		builder.append(normId);
		builder.append(").");
		builder.append(unifier);
		return builder.toString();
	}
}
