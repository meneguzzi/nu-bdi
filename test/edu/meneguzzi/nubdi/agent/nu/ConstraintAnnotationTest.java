/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the Constraint Annotation Class (Rather incomplete)
 * @author meneguzzi
 *
 */
public class ConstraintAnnotationTest {
	
	protected ConstraintAnnotation constraint1sat, constraint2sat, constraint3sat;
	protected ConstraintAnnotation constraint4unsat;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		constraint1sat = new ConstraintAnnotation(ASSyntax.parseFormula("A > 10 & A < 50"));
		constraint2sat = new ConstraintAnnotation(ASSyntax.parseFormula("40 < A & A < 200"));
		constraint3sat = new ConstraintAnnotation(ASSyntax.parseFormula("500 < B & B > 1200"));
		constraint4unsat = new ConstraintAnnotation(ASSyntax.parseFormula("A > 5 & A < 5"));
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation#isSatisfiable(jason.asSemantics.Unifier)}.
	 */
	@Test
	public void testIsSatisfiable() {
		Unifier unEmpty = new Unifier();
		//First do the basics
		assertTrue(constraint1sat.isSatisfiable(unEmpty));
		Unifier unProblem = new Unifier();
		try {
			unProblem.unifies(ASSyntax.parseVar("A"), ASSyntax.parseNumber("1"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		//assertFalse(constraint1sat.isSatisfiable(unProblem));
		
		assertTrue(constraint2sat.isSatisfiable(unEmpty));
		
		assertTrue(constraint3sat.isSatisfiable(unEmpty));
		
		assertFalse(constraint4unsat.isSatisfiable(unEmpty));
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation#compose(edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation)}.
	 */
	@Test
	public void testCompose() {
		Unifier unEmpty = new Unifier();
		//These two composed should be satisfiable
		constraint1sat.compose(constraint2sat);
		assertTrue(constraint1sat.isSatisfiable(unEmpty));
		
		//Adding this one should not change satisfiability
		constraint1sat.compose(constraint3sat);
		assertTrue(constraint1sat.isSatisfiable(unEmpty));
		
		//These four should not
		constraint1sat.compose(constraint4unsat);
		assertFalse(constraint1sat.isSatisfiable(unEmpty));
	}

}
