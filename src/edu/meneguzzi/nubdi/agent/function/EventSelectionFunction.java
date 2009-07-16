package edu.meneguzzi.nubdi.agent.function;

import jason.asSemantics.Agent;
import jason.asSemantics.Event;

import java.util.Queue;

/**
 * An interface representing AgentSpeak's event selection function.
 * 
 * @author meneguzzi
 *
 * @param <A> The specific subclass of {@link Agent}
 */
public interface EventSelectionFunction<A extends Agent> {
	public Event selectEvent(A agent, Queue<Event> events);
}
