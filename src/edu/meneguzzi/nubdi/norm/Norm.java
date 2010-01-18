/**
 * 
 */
package edu.meneguzzi.nubdi.norm;

import java.util.Iterator;

import jason.asSemantics.Agent;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.Term;
import edu.meneguzzi.nubdi.agent.nu.NuAgent;

/**
 * A norm for NuBDI, considering the format:
 * <code>&lt; NormType, TargetAgent, TargetRole, NormTarget, Restriction, 
 *            Activation, Expiration, Id &gt;</code> 
 * @author meneguzzi
 *
 */
public interface Norm {
	public enum NormType {obligation, prohibition};
	
	/**
	 * Returns the type of this norm, either obligation or expiration.
	 * @return
	 */
	public NormType getNormType();
	
	/**
	 * Returns the name of the agent to which this norm is targeted, if all
	 * agents are targeted, returns Null.
	 * 
	 * @return
	 */
	public String getTargetAgent();
	
	/**
	 * Returns the name of the role to which this norm is targeted, if all
	 * roles are targeted, returns Null.
	 * @return
	 */
	public String getTargetRole();
	
	/**
	 * Returns the target of the norm, namely the action or state being 
	 * referred to by the norm itself.
	 * 
	 * @return
	 */
	public Term getNormTarget();
	
	/**
	 * Returns the variable restrictions associated with this norm
	 * @return
	 */
	public LogicalFormula getNormRestriction();

	/**
	 * Returns whether or not this norm is activated.
	 * @return
	 */
	public boolean isActivated();
	
	/**
	 * Return whether or not the supplied agent's beliefs  support the 
	 * activation condition, and if so, returns an iterator to all the 
	 * unifiers. For a norm to yield a correct unifier, it must make the norm
	 * ground.
	 * 
	 * TODO check the details of how this activation must be.
	 * 
	 * @param agent
	 * @return
	 */
	public Iterator<Unifier> supportsActivation(NuAgent agent);
	
	/**
	 * Returns whether or not the supplied agent's beliefs
	 * support the expiration condition.
	 * 
	 * @param agent
	 * @return
	 */
	public boolean supportsExpiration(NuAgent agent);
	
	/**
	 * Returns the identifier for this norm.
	 * @return
	 */
	public String getNormId();
	
	/**
	 * Creates a norm instance through the application of a unifier.
	 * @param unifier
	 * @return
	 */
	public Norm instantiateNorm(Unifier unifier);
	
	/**
	 * Returns the unifier that created a norm instance, if any
	 * otherwise returns Null.
	 * @return
	 */
	public Unifier getUnifier();
	
	/**
	 * Returns whether or not this norm is in the scope of the supplied
	 * {@link Literal}, for the specified <em>agentId</em> and <em>roleId</em>.
	 * 
	 * @param agentId
	 * @param roleId
	 * @param literal
	 * @return
	 */
	public boolean inScope(Agent agentId, String roleId, Literal literal);
	
	/**
	 * Returns whether or not this norm is in conflict with the supplied norm.
	 * 
	 * @param n
	 * @return
	 */
	public boolean inConflict(Norm n);
}
