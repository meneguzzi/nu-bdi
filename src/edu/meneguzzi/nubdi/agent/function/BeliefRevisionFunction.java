package edu.meneguzzi.nubdi.agent.function;

import jason.asSemantics.Agent;
import jason.asSemantics.Intention;
import jason.asSyntax.Literal;

import java.util.List;

/**
 * An interface representing AgentSpeak's belief revision function.
 * @author Felipe Meneguzzi
 *
 * @param <A>
 */
public interface BeliefRevisionFunction<A extends Agent> {
	
	/**
     * This function should revise the belief base with the given literal to
     * add, to remove, and the current intention that triggered the operation.
     * 
     * <p>In its return, List[0] has the list of actual additions to
     * the belief base, and List[1] has the list of actual deletions;
     * this is used to generate the appropriate internal events. If
     * nothing change, returns null.
     */
	public List<Literal>[] reviseBeliefs(A agent, Literal beliefToAdd, 
										Literal beliefToDel, Intention i);
}
