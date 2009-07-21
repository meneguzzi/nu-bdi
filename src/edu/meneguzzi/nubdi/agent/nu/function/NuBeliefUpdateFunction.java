/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu.function;

import jason.asSemantics.Agent;
import jason.asSyntax.Literal;

import java.util.List;

import edu.meneguzzi.nubdi.agent.function.BeliefUpdateFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultBeliefUpdateFunction;
import edu.meneguzzi.nubdi.agent.nu.NuAgent;

/**
 * @deprecated This class is no longer necessary since we are doing this
 * through an action.
 * @author meneguzzi
 *
 */
public class NuBeliefUpdateFunction implements BeliefUpdateFunction<NuAgent> {
	//We use a delegate function to do the actual belief update
	protected BeliefUpdateFunction<Agent> delegateBUF;
	
	public NuBeliefUpdateFunction() {
		this.delegateBUF = new DefaultBeliefUpdateFunction();
	}

	/* (non-Javadoc)
	 * @see edu.meneguzzi.nubdi.agent.function.BeliefUpdateFunction#updateBeliefs(jason.asSemantics.Agent, java.util.List)
	 */
	@Override
	public void updateBeliefs(NuAgent agent, List<Literal> percepts) {
		//First we do the actual belief update
		delegateBUF.updateBeliefs(agent, percepts);
		//Then we add the (abstract) norms to the agent itself
		for(Literal l : percepts) {
			if(l.getFunctor().equals("norm")){
				agent.addAbstractNorm(l);
			}
		}
	}

}
