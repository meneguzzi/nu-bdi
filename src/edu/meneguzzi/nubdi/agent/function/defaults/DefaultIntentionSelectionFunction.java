package edu.meneguzzi.nubdi.agent.function.defaults;

import jason.asSemantics.Agent;
import jason.asSemantics.Intention;

import java.util.Queue;

import edu.meneguzzi.nubdi.agent.function.IntentionSelectionFunction;

public class DefaultIntentionSelectionFunction implements
		IntentionSelectionFunction<Agent> {

	public Intention selectIntention(Agent agent, Queue<Intention> intentions) {
		return intentions.poll();
	}

}
