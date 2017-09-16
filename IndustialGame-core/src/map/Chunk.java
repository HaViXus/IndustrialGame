package map;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Chunk 
{

	public int x, y;
	public Texture texture;
	public boolean isPixmap;
	public boolean textureCreated;
	private Pixmap pixmap;
	
	public Chunk(int x, int y)
	{
		this.x = x;
		this.y = y;
		isPixmap = false;
		textureCreated = false;
	}
	public Chunk(Pixmap pixmap, int x, int y)
	{
		this(x,y);
		this.pixmap = pixmap;
		isPixmap = true;
	}
	public void createTeture()
	{
		texture = new Texture(pixmap);
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		textureCreated = true;
	}
	public void setPixmap(Pixmap pixmap){ this.pixmap = pixmap; isPixmap = true;}
	
	
}
