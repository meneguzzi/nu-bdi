package edu.meneguzzi.nubdi.agent.function.defaults;

import jason.asSemantics.ActionExec;
import jason.asSemantics.Agent;

import java.util.Iterator;
import java.util.List;

import edu.meneguzzi.nubdi.agent.function.ActionSelectionFunction;

/**
 * The default implementation of the action selection function, which here is 
 * exactly like the implementation in the original Agent class 
 * @author meneguzzi
 *
 */
public class DefaultActionSelectionFunction implements ActionSelectionFunction<Agent> {

	public ActionExec selectAction(Agent agent, List<ActionExec> actList) {
		// make sure the selected Action is removed from actList
        // (do not return suspended intentions)
        synchronized (actList) {
            Iterator<ActionExec> i = actList.iterator();
            while (i.hasNext()) {
                ActionExec a = i.next();
                if (!a.getIntention().isSuspended()) {
                    i.remove();
                    return a;
                }
            }           
        }
        return null;
	}

}
