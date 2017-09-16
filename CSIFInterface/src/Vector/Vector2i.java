package Vector;

public class Vector2i
{
	public int x, y; public float lastHumidity;
	
	public Vector2i(){}
	public Vector2i(int x, int y){this.x=x; this.y=y;}
	public Vector2i(int x, int y, float lastHumidity){this(x,y); this.lastHumidity = lastHumidity;}
}
