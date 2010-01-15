import java.util.HashSet;
import java.util.Set;


public class Bin extends  Position{

	Set<Integer> accepts;
	
	public Bin(int x,int y,Set<Integer>accepts)
	{
		super(x,y);
		this.accepts=new HashSet<Integer>(accepts);
	}
	
	public boolean accepts(Bomb b)
	{
	  if (accepts.contains(b.getType()))
		  return true;
	  return false;
	}
	
	public Set<Integer> getAccepts()
	{
		return accepts;
	}
}
