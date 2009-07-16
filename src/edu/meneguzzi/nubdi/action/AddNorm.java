/**
 * 
 */
package edu.meneguzzi.nubdi.action;

import java.util.logging.Logger;

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
		
		return true;
	}

}
