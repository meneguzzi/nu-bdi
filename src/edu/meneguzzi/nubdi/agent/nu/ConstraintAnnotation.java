/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu;

import edu.meneguzzi.csp.ConstraintSolver;
import edu.meneguzzi.csp.ConstraintSolverException;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.LogicalFormula;
import jason.asSyntax.parser.ParseException;

/**
 * @author meneguzzi
 *
 */
public class ConstraintAnnotation {
	
	protected LogicalFormula constraint;
	
	/**
	 * If we use the empty constructor, we create a true annotation
	 */
	public ConstraintAnnotation() {
		try {
			this.constraint = ASSyntax.parseFormula("true");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
	
	/**
	 * Returns a new constraint consisting of this formula negated
	 * @return
	 */
	public ConstraintAnnotation negate() {
		
		try {
			LogicalFormula negated = ASSyntax.parseFormula("!("+this.constraint.toString()+")");
			return new ConstraintAnnotation(negated);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Composes two constraint annotations as the conjunction of the two logical expressions.
	 * 
	 * TODO Maybe we want to check that the entire composition is satisfiable?
	 * 
	 * @param newAnnotation
	 */
	public void compose(ConstraintAnnotation newAnnotation) {
		//TODO review the entire algorithm, this one is quick and dirty
		try {
			this.constraint = ASSyntax.parseFormula(this.constraint.toString()+" & "+newAnnotation.constraint.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
