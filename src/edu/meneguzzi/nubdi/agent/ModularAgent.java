/**
 * 
 */
package edu.meneguzzi.nubdi.agent;

import jason.JasonException;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Agent;
import jason.asSemantics.Event;
import jason.asSemantics.Intention;
import jason.asSemantics.Message;
import jason.asSemantics.Option;
import jason.asSyntax.Literal;

import java.security.acl.LastOwnerException;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

import edu.meneguzzi.nubdi.agent.function.ActionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.BeliefRevisionFunction;
import edu.meneguzzi.nubdi.agent.function.BeliefUpdateFunction;
import edu.meneguzzi.nubdi.agent.function.EventSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.IntentionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.MessageSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.OptionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultActionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultBeliefRevisionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultBeliefUpdateFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultEventSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultIntentionSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultMessageSelectionFunction;
import edu.meneguzzi.nubdi.agent.function.defaults.DefaultOptionSelectionFunction;

/**
 * A Modular Agent class extending Jason's basic agent class. Each one of
 * AgentSpeak's basic functions is delegated to instances of a strategy class.
 * @author Felipe Meneguzzi
 *
 */
public class ModularAgent extends Agent {
	
	protected static final Logger logger = Logger.getLogger(Agent.class.getName());
	
	@SuppressWarnings("rawtypes")
	protected ActionSelectionFunction actionSelectionFunction = null;
	@SuppressWarnings("rawtypes")
	protected BeliefRevisionFunction beliefRevisionFunction = null;
	@SuppressWarnings("rawtypes")
	protected BeliefUpdateFunction beliefUpdateFunction = null;
	@SuppressWarnings("rawtypes")
	protected EventSelectionFunction eventSelectionFunction = null;
	@SuppressWarnings("rawtypes")
	protected IntentionSelectionFunction intentionSelectionFunction = null;
	@SuppressWarnings("rawtypes")
	protected MessageSelectionFunction messageSelectionFunction = null;
	@SuppressWarnings("rawtypes")
	protected OptionSelectionFunction optionSelectionFunction = null;
	
	public ModularAgent() {
		//Default function implementations
		this.actionSelectionFunction = new DefaultActionSelectionFunction();
		this.beliefRevisionFunction = new DefaultBeliefRevisionFunction();
		this.beliefUpdateFunction = new DefaultBeliefUpdateFunction();
		this.eventSelectionFunction = new DefaultEventSelectionFunction();
		this.intentionSelectionFunction = new DefaultIntentionSelectionFunction();
		this.messageSelectionFunction = new DefaultMessageSelectionFunction();
		this.optionSelectionFunction = new DefaultOptionSelectionFunction();
	}
	
	/* (non-Javadoc)
	 * @see jason.asSemantics.Agent#initAg(java.lang.String)
	 */
	@Override
	public void initAg(String asSrc) throws JasonException {
		// TODO Auto-generated method stub
		super.initAg(asSrc);
		loadActionDefinitions(asSrc);
	}
	
	private final void loadActionDefinitions(String asSrc) {
		asSrc = asSrc.substring(0, asSrc.lastIndexOf('.')) + ".act";
		logger.info("Loading action definitions from "+asSrc);
		
	}
	
	//AgentSpeak functions, and the corresponding calls to delegate classes

	@SuppressWarnings("unchecked")
	@Override
	public void buf(List<Literal> percepts) {
		beliefUpdateFunction.updateBeliefs(this, percepts);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Literal>[] brf(Literal beliefToAdd, Literal beliefToDel, Intention i) {
		return this.beliefRevisionFunction.reviseBeliefs(this, beliefToAdd, beliefToDel, i);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Intention selectIntention(Queue<Intention> intentions) {
		return this.intentionSelectionFunction.selectIntention(this, intentions);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Option selectOption(List<Option> options) {
		return optionSelectionFunction.selectOption(this,options);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ActionExec selectAction(List<ActionExec> actList) {
		return this.actionSelectionFunction.selectAction(this, actList);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Event selectEvent(Queue<Event> events) {
		return this.eventSelectionFunction.selectEvent(this, events);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Message selectMessage(Queue<Message> msgList) {
		return this.messageSelectionFunction.selectMessage(this, msgList);
	}
}
