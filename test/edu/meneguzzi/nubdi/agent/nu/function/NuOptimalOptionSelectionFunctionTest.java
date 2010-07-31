/**
 * 
 */
package edu.meneguzzi.nubdi.agent.nu.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import jason.asSemantics.Option;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import jason.asSyntax.Trigger;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.meneguzzi.nubdi.agent.nu.NuAgent;
import edu.meneguzzi.nubdi.exception.NuBDIException;

/**
 * @author meneguzzi
 *
 */
public class NuOptimalOptionSelectionFunctionTest {
	
	protected static final String nuAgentCode = "test/optimalSelectionTest.asl";
	protected static final String obligation1String =
		    "norm(obligation," +
		    "action(A,B)," +
		    "A > 10," +
		    "activate(A,B)," +
		    "expire(B), 11)";
	protected static final String obligation2String = 
		    "norm(obligation, " +
			"action2(A,B), " +
			"A > 10," +
			"activate2(A,B), " +
			"expire2(B), 12)";
	
	protected static final String prohibition1String = 
	    "norm(prohibition, " +
		"action2(A,B), " +
		"A>10," +
		"activate3(A,B), " +
		"expire3(B), 13)";
	
	protected Term obligation1;
	protected Term obligation2;
	protected Term prohibition1;
	
	
	protected Literal activation1;
	protected Literal activation2;
	protected Literal activation3;
	
	protected Trigger planTrigger;
	
	protected NuAgent nuAgent;
	
	protected NuOptimalOptionSelectionFunction optimalOptionSelectionFunction = new NuOptimalOptionSelectionFunction();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		nuAgent = new NuAgent();
		nuAgent.initAg(nuAgentCode);
		
		obligation1 = ASSyntax.parseTerm(obligation1String);
		obligation2 = ASSyntax.parseTerm(obligation2String);
		prohibition1 = ASSyntax.parseTerm(prohibition1String);
		
		activation1 = ASSyntax.parseLiteral("activate(15,15)");
		activation2 = ASSyntax.parseLiteral("activate2(15,15)");
		activation3 = ASSyntax.parseLiteral("activate3(15,15)");
		
		planTrigger = ASSyntax.parseTrigger("+event(20,20)");
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.function.NuOptimalOptionSelectionFunction#selectOption(edu.meneguzzi.nubdi.agent.nu.NuAgent, java.util.List)}.
	 */
	@Test
	public void testSelectOption() {
		try {
			nuAgent.addAbstractNorm(obligation1);
			nuAgent.addAbstractNorm(obligation2);
			nuAgent.addAbstractNorm(prohibition1);
			
			nuAgent.addBel(activation1);
			nuAgent.addBel(activation2);
			nuAgent.addBel(activation3);
			
			nuAgent.updateNorms();
			
			List<Option> relevantPlans = nuAgent.getTS().relevantPlans(planTrigger);
			
			assertNotNull(relevantPlans);
			assertEquals(3, relevantPlans.size());
			
			Option selectedPlan = optimalOptionSelectionFunction.selectOption(nuAgent, relevantPlans);
			assertEquals("testPlan1", selectedPlan.getPlan().getLabel().getFunctor());
		} catch (NuBDIException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.function.NuOptimalOptionSelectionFunction#selectOption(edu.meneguzzi.nubdi.agent.nu.NuAgent, java.util.List)}.
	 */
	@Test
	public void testSelectOptionAllObligations() {
		try {
			nuAgent.addAbstractNorm(obligation1);
			nuAgent.addAbstractNorm(obligation2);
			
			nuAgent.addBel(activation1);
			nuAgent.addBel(activation2);
			
			nuAgent.updateNorms();
			
			List<Option> relevantPlans = nuAgent.getTS().relevantPlans(planTrigger);
			
			assertNotNull(relevantPlans);
			assertEquals(3, relevantPlans.size());
			
			Option selectedPlan = optimalOptionSelectionFunction.selectOption(nuAgent, relevantPlans);
			assertEquals("testPlan2", selectedPlan.getPlan().getLabel().getFunctor());
		} catch (NuBDIException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test method for {@link edu.meneguzzi.nubdi.agent.nu.function.NuOptimalOptionSelectionFunction#selectOption(edu.meneguzzi.nubdi.agent.nu.NuAgent, java.util.List)}.
	 */
	@Test
	public void testSelectOptionAllProhibitions() {
		try {
			nuAgent.addAbstractNorm(prohibition1);
			
			nuAgent.addBel(activation3);
			
			nuAgent.updateNorms();
			assertEquals(1,nuAgent.getSpecificNorms().size());
			
			List<Option> relevantPlans = nuAgent.getTS().relevantPlans(planTrigger);
			
			assertNotNull(relevantPlans);
			assertEquals(3, relevantPlans.size());
			
			Option selectedPlan = optimalOptionSelectionFunction.selectOption(nuAgent, relevantPlans);
			assertEquals("testPlan1", selectedPlan.getPlan().getLabel().getFunctor());
		} catch (NuBDIException e) {
			fail(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
