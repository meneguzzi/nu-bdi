/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu.function;

import jason.asSemantics.Option;
import jason.asSyntax.Pred;

import java.util.List;

import edu.meneguzzi.nubdi.agent.function.OptionSelectionFunction;
import edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation;
import edu.meneguzzi.nubdi.agent.nu.NuAgent;

/**
 * A customized class for selecting options based on the constraint annotations
 * coming from the norms accepted by the agent.
 * 
 * Our first implementation just selects the first option whose annotation is
 * satisfiable with the option unifier.
 * 
 * @author meneguzzi
 *
 */
public class NuOptionSelectionFunction implements
		OptionSelectionFunction<NuAgent> {

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.agent.function.OptionSelectionFunction#selectOption(jason.asSemantics.Agent, java.util.List)
	 */
	@Override
	public Option selectOption(NuAgent agent, List<Option> options) {
		Option selectedOption = null;
		for(Option option : options) {
			Pred label = option.getPlan().getLabel();
			ConstraintAnnotation constraint = agent.getAnnotationForPlan(label.toString());
			//We select any plan that has no constraint, or one that has a 
			//satisfiable constraint
			if(constraint == null || constraint.isSatisfiable(option.getUnifier())) {
				selectedOption = option;
			}
		}
		//TODO use the constraint scoring (now unimplemented) to decide on 
		//TODO options that are unsatisfiable
//		if(selectedOption == null) {
//			int selectedOptionScore = Integer.MIN_VALUE;
//			for(Option option : options) {
//				Pred label = option.getPlan().getLabel();
//				ConstraintAnnotation constraint = agent.getAnnotationForPlan(label.toString());
//				int score = constraint.scoreSatisfiability(option.getUnifier());
//				if(score > selectedOptionScore) {
//					selectedOptionScore = score;
//					selectedOption = option;
//				}
//			}
//		}
		return selectedOption;
	}

}
