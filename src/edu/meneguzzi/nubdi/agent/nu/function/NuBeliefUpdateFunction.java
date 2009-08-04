/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu.function;

import jason.asSemantics.Agent;
import jason.asSyntax.Literal;

import java.util.List;
import java.util.logging.Logger;

import edu.meneguzzi.nubdi.agent.function.BeliefUpdateFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultBeliefUpdateFunction;
import edu.meneguzzi.nubdi.agent.nu.NuAgent;

/**
 * A decorator class that appends the normative processing code after executing 
 * the belief revision from {@link DefaultBeliefUpdateFunction}. The function
 * in agent corresponds to that of our paper at <a href="https://svn.xp-dev.com/svn/meneguzzi-collab-papers/trunk/norm-bdi-2009/norm-bdi-main.pdf">XP-Dev</a>.
 * @author meneguzzi
 *
 */
public class NuBeliefUpdateFunction implements BeliefUpdateFunction<NuAgent> {
	private static Logger logger = Logger.getLogger(BeliefUpdateFunction.class.getName());
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
		logger.fine("Calling NuBeliefUpdateFunction");
		//First we do the actual belief update
		delegateBUF.updateBeliefs(agent, percepts);
		agent.updateNorms();
	}

}
