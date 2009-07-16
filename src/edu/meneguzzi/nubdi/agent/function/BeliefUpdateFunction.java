package edu.meneguzzi.nubdi.agent.function;

import jason.asSemantics.Agent;
import jason.asSyntax.Literal;

import java.util.List;

/**
 * An interface representing AgentSpeak's belief update function.
 * @author meneguzzi
 *
 * @param <A>
 */
public interface BeliefUpdateFunction<A extends Agent> {
	
	/**
	 * Belief Update Function: adds/removes percepts into belief base
	 * 
	 * @param agent
	 * @param percepts
	 */
	public void updateBeliefs(A agent, List<Literal> percepts);
}
