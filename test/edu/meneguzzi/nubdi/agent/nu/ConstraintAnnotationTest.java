/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import jason.asSemantics.Agent;
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
	
	protected static final String nuAgentCode = "testAgent1.asl";
	protected NuAgent nuAgent;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		constraint1sat = new ConstraintAnnotation(ASSyntax.parseFormula("A > 10 & A < 50"));
		constraint2sat = new ConstraintAnnotation(ASSyntax.parseFormula("40 < A & A < 200"));
		constraint3sat = new ConstraintAnnotation(ASSyntax.parseFormula("500 < B & B < 1200"));
		constraint4unsat = new ConstraintAnnotation(ASSyntax.parseFormula("A > 5 & A < 5"));
		nuAgent = new NuAgent();
		nuAgent.initAg(nuAgentCode);
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation#isSatisfiable(jason.asSemantics.Unifier)}.
	 * This method is expected to test if constraint annotations work when the constraints are not totally ground
	 */
	@Test
	public void testIsSatisfiableNonGround() {
		Unifier unEmpty = new Unifier();
		//First do the basics
		assertTrue(constraint1sat.isSatisfiable(unEmpty,nuAgent));
		Unifier unProblem = new Unifier();
		try {
			unProblem.unifies(ASSyntax.parseVar("A"), ASSyntax.parseNumber("1"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		//assertFalse(constraint1sat.isSatisfiable(unProblem));
		
		assertTrue(constraint2sat.isSatisfiable(unEmpty,nuAgent));
		
		assertTrue(constraint3sat.isSatisfiable(unEmpty,nuAgent));
		
		assertFalse(constraint4unsat.isSatisfiable(unEmpty,nuAgent));
	}
	
	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation#isSatisfiable(jason.asSemantics.Unifier)}.
	 */
	@Test
	public void testIsSatisfiable() {
		Unifier unProblem = new Unifier();
		try {
			unProblem.unifies(ASSyntax.parseVar("A"), ASSyntax.parseNumber("45"));
			unProblem.unifies(ASSyntax.parseVar("B"), ASSyntax.parseNumber("600"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		//First do the basics
		assertTrue(constraint1sat.isSatisfiable(unProblem,nuAgent));
		
		assertTrue(constraint2sat.isSatisfiable(unProblem,nuAgent));
		
		assertTrue(constraint3sat.isSatisfiable(unProblem,nuAgent));
		
		assertFalse(constraint4unsat.isSatisfiable(unProblem,nuAgent));
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation#compose(edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation)}.
	 */
	@Test
	public void testComposeNonGround() {
		Unifier unEmpty = new Unifier();
		//These two composed should be satisfiable
		constraint1sat.compose(constraint2sat);
		assertTrue(constraint1sat.isSatisfiable(unEmpty,nuAgent));
		
		//Adding this one should not change satisfiability
		constraint1sat.compose(constraint3sat);
		assertTrue(constraint1sat.isSatisfiable(unEmpty,nuAgent));
		
		//These four should not
		constraint1sat.compose(constraint4unsat);
		assertFalse(constraint1sat.isSatisfiable(unEmpty,nuAgent));
	}
	
	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation#compose(edu.meneguzzi.nubdi.agent.nu.ConstraintAnnotation)}.
	 */
	@Test
	public void testCompose() {
		Unifier unProblem = new Unifier();
		try {
			unProblem.unifies(ASSyntax.parseVar("A"), ASSyntax.parseNumber("45"));
			unProblem.unifies(ASSyntax.parseVar("B"), ASSyntax.parseNumber("600"));
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		//These two composed should be satisfiable
		constraint1sat.compose(constraint2sat);
		assertTrue(constraint1sat.isSatisfiable(unProblem,nuAgent));
		
		//Adding this one should not change satisfiability
		constraint1sat.compose(constraint3sat);
		assertTrue(constraint1sat.isSatisfiable(unProblem,nuAgent));
		
		//These four should not
		constraint1sat.compose(constraint4unsat);
		assertFalse(constraint1sat.isSatisfiable(unProblem,nuAgent));
	}

}
