
public class Bomb extends Moveable {
	int type;
	
	public Bomb(int x,int y, int type)
	{
		super(x,y);
		this.type=type;
	}
	
	public int getType(){return type;}

}
