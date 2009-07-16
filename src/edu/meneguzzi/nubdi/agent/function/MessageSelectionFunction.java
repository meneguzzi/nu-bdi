package edu.meneguzzi.nubdi.agent.function;

import jason.asSemantics.Agent;
import jason.asSemantics.Message;

import java.util.Queue;

public interface MessageSelectionFunction<A extends Agent> {
	public Message selectMessage(A agent, Queue<Message> msgList);
}
