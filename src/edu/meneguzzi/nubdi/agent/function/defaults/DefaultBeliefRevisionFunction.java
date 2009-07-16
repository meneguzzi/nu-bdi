package edu.meneguzzi.nubdi.agent.function.defaults;

import jason.asSemantics.Agent;
import jason.asSemantics.Intention;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.meneguzzi.nubdi.agent.function.BeliefRevisionFunction;

public class DefaultBeliefRevisionFunction implements BeliefRevisionFunction<Agent> {
	private static final Logger logger = Logger.getLogger(Agent.class.getName());

	@SuppressWarnings("unchecked")
	public List<Literal>[] reviseBeliefs(Agent agent, Literal beliefToAdd,
			Literal beliefToDel, Intention i) {
		// This class does not implement belief revision! It
        // is supposed that a subclass will do it.
        // It simply add/del the belief.

        List<Literal>[] result = null;
        try {
            if (beliefToAdd != null) {
                if (logger.isLoggable(Level.FINE)) logger.fine("adding belief " + beliefToAdd);
                
                if (agent.getBB().add(beliefToAdd)) {
                    result = new List[2];
                    result[0] = Collections.singletonList(beliefToAdd);
                    result[1] = Collections.emptyList();
                }
            }
    
            if (beliefToDel != null) {
                Unifier u = null;
                try {
                    u = i.peek().getUnif(); // get from current intention
                } catch (Exception e) {
                    u = new Unifier();
                }
    
                if (logger.isLoggable(Level.FINE)) logger.fine("doing brf for " + beliefToDel + " in BB=" + agent.believes(beliefToDel, u));
                
                if (agent.believes(beliefToDel, u)) {
                    beliefToDel.apply(u);
                    if (agent.getBB().remove(beliefToDel)) {
                        if (logger.isLoggable(Level.FINE)) logger.fine("Removed:" + beliefToDel);
                        if (result == null) {
                            result = new List[2];
                            result[0] = Collections.emptyList();
                        }
                        result[1] = Collections.singletonList(beliefToDel);
                    }
                }
    
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error at BRF.",e);
        }
        return result;
	}

}
