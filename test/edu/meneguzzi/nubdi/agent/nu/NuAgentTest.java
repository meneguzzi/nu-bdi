/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import jason.asSemantics.Unifier;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Term;

import org.junit.Before;
import org.junit.Test;

import edu.meneguzzi.nubdi.exception.NuBDIException;
import edu.meneguzzi.nubdi.norm.Norm;
import edu.meneguzzi.nubdi.norm.NormImpl;

/**
 * @author meneguzzi
 *
 */
public class NuAgentTest {
	
	protected static final String nuAgentCode = "test/testAgent1.asl";
	protected static final String obligation1String =
		    "norm(obligation," +
		    "action(A,B)," +
		    "A > 10," +
		    "activate(A,B)," +
		    "expire(B), 11).";
	protected static final String obligation2String = 
		    "norm(obligation, " +
			"evacuate(P,X,Y), " +
			"\"10<=X & X<=40 & 20<=Y & Y<=80\"," +
			"\"at_loc(P,X) & unsafe(X) & safe(Y)\", " +
			"emergency_level(X,low), 12).";
	
	protected Term obligation1;
	protected Term obligation2;
	
	protected NuAgent nuAgent;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		nuAgent = new NuAgent();
		nuAgent.initAg(nuAgentCode);
		
		obligation1 = ASSyntax.parseTerm(obligation1String);
		obligation2 = ASSyntax.parseTerm(obligation2String);
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.NuAgent#addAbstractNorm(jason.asSyntax.Term)}.
	 */
	@Test
	public void testAddAbstractNorm() {
		try {
			assertTrue(nuAgent.addAbstractNorm(obligation1));
			assertFalse(nuAgent.addAbstractNorm(obligation1));
			assertTrue(nuAgent.addAbstractNorm(obligation2));
			assertFalse(nuAgent.addAbstractNorm(obligation2));
		} catch (NuBDIException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.NuAgent#addSpecificNorm(edu.meneguzzi.nubdi.norm.Norm)}.
	 */
	@Test
	public void testAddSpecificNorm() {
		try {
			//First we add the abstract norm to the agent
			assertTrue(nuAgent.addAbstractNorm(obligation1));
			Norm norm = new NormImpl(obligation1);
			//Then we try to find it among those norms
			for(Norm n : nuAgent.getAbstractNorms()) {
				if(n.getNormId() == norm.getNormId()) {
					Unifier un = new Unifier();
					un.unifies(ASSyntax.parseTerm("activate(a,b)"), ASSyntax.parseTerm("activate(A,B)"));
					Norm specificNorm = n.instantiateNorm(un);
					assertTrue(nuAgent.addSpecificNorm(specificNorm));
					assertNotNull(nuAgent.getSpecificNorms().contains(specificNorm));
				}
			}
			
			assertTrue(nuAgent.addAbstractNorm(obligation2));
			norm = new NormImpl(obligation2);
			for(Norm n : nuAgent.getAbstractNorms()) {
				if(n.getNormId() == norm.getNormId()) {
//					Unifier un = new Unifier();
//					un.unifies(ASSyntax.parseTerm("activate(a,b)"), ASSyntax.parseTerm("activate(A,B)"));
//					Norm specificNorm = n.instantiateNorm(un);
//					assertTrue(nuAgent.addSpecificNorm(specificNorm));
//					assertNotNull(nuAgent.getSpecificNorms().contains(specificNorm));
				}
			}
		} catch (NuBDIException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.NuAgent#removeAbstractNorm(java.lang.String)}.
	 */
	@Test
	public void testRemoveAbstractNorm() {
		try {
			assertTrue(nuAgent.addAbstractNorm(obligation1));
			Norm norm = new NormImpl(obligation1);
			assertNotNull(nuAgent.removeAbstractNorm(norm.getNormId()));
			
			assertTrue(nuAgent.addAbstractNorm(obligation2));
			norm = new NormImpl(obligation2);
			assertNotNull(nuAgent.removeAbstractNorm(norm.getNormId()));
		} catch (NuBDIException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.NuAgent#removeSpecificNorm(java.lang.String)}.
	 */
	@Test
	public void testRemoveSpecificNorm() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.NuAgent#updateNorms()}.
	 */
	@Test
	public void testUpdateNorms() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.NuAgent#resolveNormConflict(edu.meneguzzi.nubdi.norm.Norm, edu.meneguzzi.nubdi.norm.Norm)}.
	 */
	@Test
	public void testResolveNormConflict() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.NuAgent#annotatePlans()}.
	 */
	@Test
	public void testAnnotatePlans() {
		try {
			assertEquals(0,nuAgent.getSpecificNorms().size());
			assertTrue(nuAgent.addAbstractNorm(obligation1));
			assertTrue(nuAgent.addBel(ASSyntax.parseLiteral("activate(12,15)")));
			//Annotate plans is tested through updateNorms
			assertTrue(nuAgent.updateNorms());
			assertEquals(1,nuAgent.getSpecificNorms().size());
			ConstraintAnnotation annotation = nuAgent.getAnnotationForPlan("testPlan1[source(self)]");
			assertNotNull(annotation);
			assertEquals(new ConstraintAnnotation(ASSyntax.parseFormula("12 > 10")), annotation);
			
			assertTrue(nuAgent.addAbstractNorm(obligation2));
			assertTrue(nuAgent.addBel(ASSyntax.parseLiteral("at_loc(annotated,15)")));
			assertTrue(nuAgent.addBel(ASSyntax.parseLiteral("unsafe(15)")));
			assertTrue(nuAgent.addBel(ASSyntax.parseLiteral("safe(30)")));
			assertTrue(nuAgent.updateNorms());
			assertEquals(2,nuAgent.getSpecificNorms().size());
		} catch (NuBDIException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.NuAgent#annotatePlan(jason.asSyntax.Plan)}.
	 */
	@Test
	public void testAnnotatePlan() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testNormActivation() {
		
	}

}
