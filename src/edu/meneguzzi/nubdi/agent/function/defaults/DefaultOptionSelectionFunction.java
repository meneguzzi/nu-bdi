package edu.meneguzzi.nubdi.agent.function.defaults;

import jason.asSemantics.Agent;
import jason.asSemantics.Option;

import java.util.List;

import edu.meneguzzi.nubdi.agent.function.OptionSelectionFunction;

public class DefaultOptionSelectionFunction implements OptionSelectionFunction<Agent> {
	
	public Option selectOption(Agent agent, List<Option> options) {
		if (options.size() > 0) {
            return options.remove(0);
        } else {
            return null;
        }
	}

}
