/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu;

import edu.meneguzzi.csp.ConstraintSolver;
import edu.meneguzzi.csp.ConstraintSolverException;
import jason.asSemantics.Unifier;
import jason.asSyntax.LogicalFormula;

/**
 * @author meneguzzi
 *
 */
public class ConstraintAnnotation {
	
	public LogicalFormula constraint;
	
	
	public ConstraintAnnotation(LogicalFormula constraint) {
		this.constraint=constraint;
	}
	
	/**
	 * Returns whether or not this annotation is satisfiable given the 
	 * supplied {@link Unifier}.
	 * @param unifier
	 * @return
	 */
	public boolean isSatisfiable(Unifier unifier) {
		ConstraintSolver solver = ConstraintSolver.createConstraintSolver();
		
		LogicalFormula formula = (LogicalFormula) constraint.clone();
		
		boolean satisfiable = false;
		try {
			satisfiable = solver.satisfiable(formula);
		} catch (ConstraintSolverException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return satisfiable;
	}
	
	
}
