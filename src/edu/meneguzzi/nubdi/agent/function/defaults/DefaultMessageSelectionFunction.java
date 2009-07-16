package edu.meneguzzi.nubdi.agent.function.defaults;

import jason.asSemantics.Agent;
import jason.asSemantics.Message;

import java.util.Queue;

import edu.meneguzzi.nubdi.agent.function.MessageSelectionFunction;

public class DefaultMessageSelectionFunction implements
		MessageSelectionFunction<Agent> {

	public Message selectMessage(Agent agent, Queue<Message> msgList) {
		//make sure the selected Message is removed from msgList
		return msgList.poll();
	}

}
