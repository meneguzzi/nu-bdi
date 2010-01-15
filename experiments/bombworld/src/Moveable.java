
public class Moveable extends Position {
	public Moveable(int x,int y)
	{
		super(x,y);
	}
	
	public void move(String direction)
	{
		if (direction.equals("n"))
			y++;
		if (direction.equals("s"))
			y--;
		if (direction.equals("e"))
			x++;
		if (direction.equals("w"))
			x--;
		
	}

}
