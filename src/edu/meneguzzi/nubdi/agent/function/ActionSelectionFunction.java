package edu.meneguzzi.nubdi.agent.function;

import jason.asSemantics.ActionExec;
import jason.asSemantics.Agent;

import java.util.List;

/**
 * An interface representing AgentSpeak's action selection function.
 * @author Felipe Meneguzzi
 *
 * @param <A>
 */
public interface ActionSelectionFunction<A extends Agent> {
	/**
	 * Selects an action for execution from among the list of pending actions.
	 * 
	 * @param agent
	 * @param actList
	 * @return
	 */
	public ActionExec selectAction(A agent, List<ActionExec> actList);
}
