/**
 * 
 */
package test.act;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.asSyntax.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

import org.kcl.jason.env.action.ExternalAction;
import org.kcl.jason.env.scripted.ScriptedEnvironment;

/**
 * @author meneguzzi
 *
 */
public class move2 implements ExternalAction<ScriptedEnvironment> {

	/* (non-Javadoc)
	 * @see org.kcl.jason.env.action.ExternalAction#consequences(jason.environment.Environment, java.lang.String, jason.asSyntax.Term[])
	 */
	@Override
	public List<Literal> consequences(ScriptedEnvironment env, String agent, Term... args) {
		List<Literal> res = null;
		try {
			Term agentTerm = ASSyntax.parseTerm(agent);
			res = new ArrayList<Literal>();
			Literal lit = ASSyntax.createLiteral(true, "at_loc", new Term[] {agentTerm, args[1]});
			res.add(lit);
			lit = ASSyntax.createLiteral(false, "at_loc", new Term[] {agentTerm, args[0]});
			res.add(lit);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return res;
	}

	/* (non-Javadoc)
	 * @see org.kcl.jason.env.action.ExternalAction#execute(jason.environment.Environment, java.lang.String, jason.asSyntax.Term[])
	 */
	@Override
	public boolean execute(ScriptedEnvironment env, String agent, Term... args) {
		logger.info("Executing "+getFunctor()+"/"+args.length);
		if(args.length == 2) {
			List<Literal> effects = consequences(env, agent, args);
			env.addPercepts(effects);
			return true;
		} else {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.kcl.jason.env.action.ExternalAction#execute(jason.environment.Environment, java.lang.String, jason.asSyntax.Structure)
	 */
	@Override
	public boolean execute(ScriptedEnvironment env, String agName,
			Structure invocation) {
		return this.execute(env, agName, invocation.getTermsArray());
	}

	/* (non-Javadoc)
	 * @see org.kcl.jason.env.action.ExternalAction#getFunctor()
	 */
	@Override
	public String getFunctor() {
		return "move";
	}

}
