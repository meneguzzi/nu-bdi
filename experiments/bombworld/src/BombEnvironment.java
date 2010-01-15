
import java.util.logging.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;

public class BombEnvironment extends Environment {
	private static Logger logger = Logger.getLogger("BombEnvironment");

	
	Map<String,Agent> agents=new HashMap<String,Agent>();
	Set<Bomb> bombs=new HashSet<Bomb>();
	Set<Bin> bins=new HashSet<Bin>();
	Set<Unsafe> unsafe=new HashSet<Unsafe>();
	
	//This constructor is just meant to test the basic environment
	public BombEnvironment()
	{
		Set<Integer> t=new HashSet<Integer>();
		t.add(1);
		this.addBin(3, 3, t);
		
		this.addBomb(8, 8, 1);
		
		this.addAgent("bombagent", 4, 4);
		
		this.addUnsafe(5,4);
		
		for (Literal l:this.getAllPercepts())
			this.addPercept(l);
		
	}
	
	public void addUnsafe(int x,int y)
	{
		unsafe.add(new Unsafe(x,y));
	}
	
	public void addAgent(String name,int x, int y)
	{
		Agent a=new Agent(name,x,y);
		agents.put(name,a);
	}
	
	public void addBomb(int x,int y, int type)
	{
		bombs.add(new Bomb(x,y,type));
	}
	
	public void addBin(int x,int y, Set<Integer> types)
	{
		bins.add(new Bin(x,y,types));
	}
	
	public boolean executeAction(String agentName,Structure act)
	{
		Set<Literal> oldPercepts=getAllPercepts();
		
		Agent a=agents.get(agentName);
		if (act.getFunctor().equals("move"))
		  a.move(act.getTerm(0).toString());
		if (act.getFunctor().equals("pickup"))
		  if (!pickupBomb(a))
			  return false;
		if (act.getFunctor().equals("drop"))
		  if (!dropBomb(a))
			  return false;
		
		Set<Literal> newPercepts=getAllPercepts();
		
		for (Literal l:newPercepts)
			if (!oldPercepts.contains(l))
				this.addPercept(l);
		for (Literal l:oldPercepts)
			if (!newPercepts.contains(l))
				this.removePercept(l);
		
	  return true;
	}
	
	//tries to pick up the bomb at the location of agent a. Returns false if no bomb exists
	public boolean pickupBomb(Agent a)
	{
		for (Bomb b:bombs)
			if (b.getX()==a.getX() && b.getY()==a.getY())
			{ 
				a.setCarrying(b);
				return true;
			}
		return false;	
	}
	
	public boolean dropBomb(Agent a)
	{
		if (a.getCarrying()==null)
			return false;
		for (Bomb b:bombs) //can't drop bombs on other bombs
			if (b.getX()==a.getX() && b.getY()==a.getY() && a.getCarrying()!=b)
				return false;
		
		Bomb bomb=a.getCarrying();
		a.setCarrying(null);
		//notify bins
		for (Bin b:bins)
		    if (b.getX()==bomb.getX() && b.getY()==bomb.getY() && b.accepts(bomb))
		    {
		    	logger.info(""+bomb);
		    	logger.info(""+bombs);
		    	logger.info(""+((HashSet<Bomb>) bombs).remove(bomb));
		    	//can add a break here.
		    }
		
		return true;
	}
	
	public Set<Literal> getAllPercepts()
	{
		Set<Literal> ret=new HashSet<Literal>();
		ret.addAll(this.getAgentCarryLiterals());
		ret.addAll(this.getAgentPositionLiterals());
		ret.addAll(this.getBinLiterals());
		ret.addAll(this.getBombLiterals());
		ret.addAll(this.getUnsafeLiterals());
		return ret;

	}
	
	public Set<Literal> getBombLiterals()
	{
		Set<Literal> ret=new HashSet<Literal>();
		for (Bomb b:bombs)
		{
			ret.add(Literal.parseLiteral("bomb("+b.getX()+","+b.getY()+","+b.getType()+")"));
		}
		return ret;
	}
	
	public Set<Literal> getUnsafeLiterals()
	{
		Set<Literal> ret=new HashSet<Literal>();
		for (Unsafe u:unsafe)
		{
			ret.add(Literal.parseLiteral("unsafe("+u.getX()+","+u.getY()+")"));
		}
		return ret;
	}
	
	public Set<Literal> getBinLiterals()
	{
		Set<Literal> ret=new HashSet<Literal>();
		for (Bin b:bins)
		{
			for (Integer i:b.getAccepts())
			  ret.add(Literal.parseLiteral("bin("+b.getX()+","+b.getY()+","+i+")"));
		}
		return ret;
	}
	
	public Set<Literal> getAgentPositionLiterals()
	{
		Set<Literal> ret=new HashSet<Literal>();
		for (Agent a:agents.values())
		{
			ret.add(Literal.parseLiteral("agent("+a.getName()+","+a.getX()+","+a.getY()+")"));
		}
		return ret;
	}
	
	public Set<Literal> getAgentCarryLiterals()
	{
		Set<Literal> ret=new HashSet<Literal>();
		for (Agent a:agents.values())
		{
			if (a.getCarrying()!=null)
				ret.add(Literal.parseLiteral("carrying("+a.getName()+")"));
		}
		return ret;
	}
	
}
