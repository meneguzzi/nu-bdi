/**
 * 
 */
package edu.meneguzzi.nubdi.action;

import java.util.logging.Logger;

import edu.meneguzzi.nubdi.agent.nu.NuAgent;

import jason.asSemantics.Agent;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**
 * @author meneguzzi
 *
 */
public class AddNorm extends DefaultInternalAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static final Logger logger = Logger.getLogger(Agent.class.getName());
	
	/* (non-Javadoc)
	 * @see jason.asSemantics.DefaultInternalAction#execute(jason.asSemantics.TransitionSystem, jason.asSemantics.Unifier, jason.asSyntax.Term[])
	 */
	@Override
	public Object execute(TransitionSystem ts, Unifier un, Term[] args)
			throws Exception {
		
		logger.info("Adding "+ args[0]);
		Agent ag = ts.getAg();
		if(ag instanceof NuAgent) {
			NuAgent nuAgent = (NuAgent) ag;
			return nuAgent.addAbstractNorm(args[0]);
		} else {
			throw new Exception("Agent executing action is not a normative agent");
		}
	}

}
