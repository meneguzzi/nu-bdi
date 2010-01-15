
public class Agent extends Moveable{
	String name;
	
	public Agent(String name,int x, int y) {
		super(x, y);
		this.name=name;
	}

	Bomb carrying;
	
	@Override
	public void move(String direction)
	{
		if (carrying!=null)
			carrying.move(direction);
		super.move(direction);
	}
	
	public void setCarrying(Bomb b)
	{
		carrying=b;
	}
	
	public Bomb getCarrying()
	{
		return carrying;
	}
	
	public String getName(){return name;}

}
