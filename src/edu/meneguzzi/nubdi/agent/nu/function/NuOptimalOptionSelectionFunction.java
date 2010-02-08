/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu.function;

import jason.asSemantics.Option;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Plan;
import jason.asSyntax.PlanBody;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import edu.meneguzzi.nubdi.agent.function.OptionSelectionFunction;
import edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation;
import edu.meneguzzi.nubdi.agent.nu.NuAgent;
import edu.meneguzzi.nubdi.norm.Norm;

/**
 * @author meneguzzi
 *
 */
public class NuOptimalOptionSelectionFunction implements
		OptionSelectionFunction<NuAgent> {
	
	//TODO Parameterize this norm violation limit
	protected int normViolationLimit = 1;
	
	//TODO Parameterize this base benefit on a plan by plan basis
	protected int basePlanBenefit = 1;
	
	//TODO Parameterize the benefit/penalty of compliance/violation
	protected int defaultNormBenefit = 1;

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.agent.function.OptionSelectionFunction#selectOption(jason.asSemantics.Agent, java.util.List)
	 */
	@Override
	public Option selectOption(NuAgent agent, List<Option> options) {
		Option bestPlan = null;
		Unifier bestUnifier = null;
		int maxScore = Integer.MIN_VALUE;
		Collection<Norm> norms = new HashSet<Norm>(agent.getSpecificNorms());
		//XXX Question for Nir - scorePlan returns utility,normset,varphi(unifier)
		//XXX But the select best plan thing seems to get p', score. sigmaS (unifier)
		
		for(Option option:options) {
			Unifier unifier = option.getUnifier().clone();
			int score = scorePlan(agent,option,norms,unifier,normViolationLimit);
			if(score > maxScore) {
				bestPlan = option;
				bestUnifier = unifier;
			}
		}
		//  score = scorePlan(plan,norms,unifier,limit)
		bestPlan.getUnifier().compose(bestUnifier);
		
		return bestPlan;
	}
	
	/**
	 * Scores the plan contained in the supplied {@link Option}
	 * @param agent
	 * @param option
	 * @return
	 */
	protected int scorePlan(NuAgent agent, Option option, Collection<Norm> remainingNorms, Unifier unifier, int violationLimit) {
		if(violationLimit < 0) {
			return Integer.MIN_VALUE;
		}
		ConstraintAnnotation constraint = agent.annotatePlan(option.getPlan(), remainingNorms);
		int utility = 0;
		//If the norm was satisfiable (or it was true i.e. constraint == null)
		if(constraint == null || constraint.isSatisfiable(option.getUnifier(), agent)) {
			Plan plan = option.getPlan();
			utility = getBasePlanBenefit(plan);
			//If the constraint is null then it means there is no norm in the scope of this plan
			//which in turn means that the benefit of this option is simply the base benefit
			if(constraint != null) {
				//Now we check how many norms we are satisfying with this plan
				for(Norm norm:remainingNorms) {
					for(PlanBody step = plan.getBody(); step!=null; step=step.getBodyNext()) {
						if(step.getBodyTerm()!=null && norm.inScope(agent, null, (Literal)step.getBodyTerm())) {
							utility+=getNormBenefit(norm);
						}
					}
				}
			}
		} else {
			utility = Integer.MIN_VALUE;
			Collection<Norm> normSet = new HashSet<Norm>();
			Unifier varPhi = unifier.clone();
			for(Norm norm:remainingNorms) {
				Collection<Norm> tempNorms = new HashSet<Norm>(remainingNorms);
				tempNorms.remove(norm);
				int costNow = scorePlan(agent, option, tempNorms, varPhi, violationLimit - 1);
				if(costNow > utility) {
					utility = costNow;
					normSet.clear();
					normSet.addAll(tempNorms);
				}
			}
			remainingNorms.clear();
			remainingNorms.addAll(normSet);
		}
		
		return utility;
	}

	/**
	 * Returns the base benefit of the supplied plan.
	 * TODO Make this method actually evaluate the supplied plan.
	 * @param plan
	 * @return
	 */
	protected int getBasePlanBenefit(Plan plan) {
		return basePlanBenefit;
	}
	
	/**
	 * Returns the benefit of obeying the supplied norm.
	 * @param norm
	 * @return
	 */
	protected int getNormBenefit(Norm norm) {
		return defaultNormBenefit;
	}
}
