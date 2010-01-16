
import jason.asSyntax.ASSyntax;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.asSyntax.Term;
import jason.asSyntax.parser.ParseException;
import jason.environment.Environment;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

public class BombEnvironment extends Environment {
	private static Logger logger = Logger.getLogger("BombEnvironment");

	
	Map<String,Agent> agents=new HashMap<String,Agent>();
	Set<Bomb> bombs=new HashSet<Bomb>();
	Set<Bin> bins=new HashSet<Bin>();
	Set<Unsafe> unsafe=new HashSet<Unsafe>();
	
	Set<Unsafe> unsafeChange=new HashSet<Unsafe>();
	long unsafeChangeTime;
	
	//This constructor is just meant to test the basic environment
	public BombEnvironment()
	{
//		Set<Integer> t=new HashSet<Integer>();
//		t.add(1);
//		this.addBin(3, 3, t);
//		
//		this.addBomb(8, 8, 1);
//		
//		this.addAgent("bombagent", 4, 4);
//		
//		this.addUnsafe(5,4);
		if(!configEnvironment("bombworld.properties")) {
			logger.info("Failed loading properties");
		}
		
		for (Literal l:this.getAllPercepts())
			this.addPercept(l);
	}
	
	private synchronized boolean configEnvironment(String propertiesFile) {
		Properties props = new Properties();
		try {
			props.load(new FileReader(propertiesFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		boolean retValue = true;
		try {
			String binPositions = props.getProperty("bins");
			if(binPositions != null) {
				ListTerm list = ASSyntax.parseList(binPositions);
				for(Term t : list) {
					int x = Integer.parseInt(((Structure)t).getTerm(0).toString());
					int y = Integer.parseInt(((Structure)t).getTerm(1).toString());
					int type = Integer.parseInt(((Structure)t).getTerm(2).toString());
					Set<Integer> ts=new HashSet<Integer>();
					ts.add(type);
					this.addBin(x, y, ts);
				}
			} else {
				logger.warning("Failed to find and/or parse bin positions");
				retValue = false;
			}
			
			String bombPositions = props.getProperty("bombs");
			if(bombPositions != null) {
				ListTerm list = ASSyntax.parseList(bombPositions);
				for(Term t : list) {
					int x = Integer.parseInt(((Structure)t).getTerm(0).toString());
					int y = Integer.parseInt(((Structure)t).getTerm(1).toString());
					int type = Integer.parseInt(((Structure)t).getTerm(2).toString());
					this.addBomb(x, y, type);
				}
			} else {
				logger.warning("Failed to find and/or parse bomb positions");
				retValue = false;
			}
			
			String agentPosition = props.getProperty("agent");
			if(agentPosition != null) {
				Literal l = ASSyntax.parseLiteral(agentPosition);
				int x = Integer.parseInt(l.getTerm(0).toString());
				int y = Integer.parseInt(l.getTerm(1).toString());
				this.addAgent("bombagent", x, y);
			} else {
				logger.warning("Failed to find and/or parse agent position");
				retValue = false;
			}
			
			String unsafePositions = props.getProperty("unsafes");
			if(unsafePositions != null) {
				ListTerm list = ASSyntax.parseList(unsafePositions);
				for(Term t : list) {
					int x = Integer.parseInt(((Structure)t).getTerm(0).toString());
					int y = Integer.parseInt(((Structure)t).getTerm(1).toString());
					this.addUnsafe(x, y);
				}
			} else {
				logger.warning("Failed to find and/or parse unsafe positions");
				retValue = false;
			}
			
			unsafePositions = props.getProperty("unsafesChange");
			if(unsafePositions != null) {
				ListTerm list = ASSyntax.parseList(unsafePositions);
				for(Term t : list) {
					int x = Integer.parseInt(((Structure)t).getTerm(0).toString());
					int y = Integer.parseInt(((Structure)t).getTerm(1).toString());
					this.addUnsafeChange(x, y);
				}
			} else {
				logger.warning("Failed to find and/or parse unsafe positions change");
				//this is not necessarily an error
			}
			
			String unsageChangeTimeS = props.getProperty("unsagesChangeTime");
			if(unsageChangeTimeS != null) {
				this.unsafeChangeTime = Long.parseLong(unsageChangeTimeS);
				this.unsafeChangeTime += System.currentTimeMillis();
			} else {
				logger.warning("Failed to find and/or parse unsafe change time");
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retValue;
	}

	public void addUnsafe(int x,int y)
	{
		logger.info("Adding unsafe at "+x+","+y);
		unsafe.add(new Unsafe(x,y));
	}
	
	/**
	 * @param x
	 * @param y
	 */
	private void addUnsafeChange(int x, int y) {
		logger.info("Adding unsafeChange at "+x+","+y);
		unsafeChange.add(new Unsafe(x,y));
	}
	
	public void addAgent(String name,int x, int y)
	{
		logger.info("Adding agent '"+name+"' at "+x+","+y);
		Agent a=new Agent(name,x,y);
		agents.put(name,a);
	}
	
	public void addBomb(int x,int y, int type)
	{
		logger.info("Adding bomb at "+x+","+y+" of type "+type);
		bombs.add(new Bomb(x,y,type));
	}
	
	public void addBin(int x,int y, Set<Integer> types)
	{
		logger.info("Adding bin at "+x+","+y+" of types "+types);
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
		
		if(unsafeChangeTime != 0 && System.currentTimeMillis() > unsafeChangeTime) {
			logger.info("Changing unsafe squares from: "+this.unsafe+" to "+this.unsafeChange);
			unsafeChangeTime = 0;
			this.unsafe.clear();
			this.unsafe.addAll(unsafeChange);
		}
		
		Set<Literal> newPercepts=getAllPercepts();
		//logger.info("New percepts are: "+newPercepts);
		
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
		logger.info("Executing 'pickup'");
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
		logger.info("Executing 'drop'");
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
		    	Set<Bomb> tmpB=new HashSet<Bomb>();
		    	for (Bomb bb:bombs)
		    	{
		    		if (bb!=bomb)
		    			tmpB.add(bb);
		    	}
		    	bombs=tmpB;
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
