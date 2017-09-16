package camera;

import javax.swing.JOptionPane;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameCamera extends OrthographicCamera
{
	
	public boolean isMapSet = false;
	
	private int mapWidth = 0;
	private int mapHeight = 0;
	private int lastViewportWidth, lastViewportHeight;
	//private int viewportWidth, viewportHeight;
	
	public GameCamera(int viewportWidth, int viewportHeight)
	{
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		lastViewportWidth = viewportWidth;
		lastViewportHeight = viewportHeight;
		this.position.set((int)(viewportWidth/2), (int)(viewportHeight/2), 0);	
	}

	public GameCamera(int viewportWidth, int viewportHeight, int mapWidth, int mapHeight)
	{
		this(viewportWidth, viewportHeight);
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;		
		isMapSet = true;
	}
	
	public void translateCamera(float deltaX, float deltaY, float deltaTime)
	{
		
		float dx = deltaX * deltaTime;
		float dy = deltaY * deltaTime;
		
		int halfWidth = (int) (viewportWidth/2);
		int halfHeight = (int) (viewportHeight/2);
		
		
		
        if(isMapSet == true)
        {
        	this.translate(dx * this.zoom, dy * this.zoom);
        	
        	if(mapWidth >= mapHeight)
    		{
    			if(this.viewportWidth*this.zoom > mapWidth)
    				this.zoom = (float)(mapWidth/viewportWidth);
    		}
    		else
    		{
    			if(this.viewportHeight*this.zoom > mapHeight)
    				this.zoom = (float)(mapHeight/viewportHeight);
    		}
    		
    		if(this.position.x <= halfWidth*this.zoom)
    		{
    			if(dx<0) dx=0;
    		   
    		    if(this.position.x < halfWidth*this.zoom)
    		    	this.position.set(halfWidth*this.zoom, this.position.y, 0);
    		}
    		else if(this.position.x + halfWidth*this.zoom >= mapWidth)
    		{
    			if(dx>0) dx=0;
    			
    			if(this.position.x + halfWidth*this.zoom> mapWidth)
    				this.position.set(mapWidth - halfWidth*this.zoom, this.position.y, 0);
    		}
    		if(this.position.y <= halfHeight*this.zoom)
    		{
    			if(dy<0)dy=0;
    			if(this.position.y < halfHeight*this.zoom)
    				this.position.set(this.position.x, halfHeight*this.zoom, 0);
    			
    		}
    		else if(this.position.y + halfHeight*this.zoom >= mapHeight)
    		{
    			if(dy>0) dy=0;
    			if(this.position.y + halfHeight*this.zoom > mapHeight )
    				this.position.set(this.position.x, mapHeight - halfHeight*this.zoom, 0);
    		}
        }

	}
	
	public void setMapSize(int mapWidth, int mapHeight)
	{
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		isMapSet = true;
	}
	
	public void updateResolution(int viewportWidth, int viewportHeight)
	{
		if(viewportWidth != lastViewportWidth || viewportHeight != lastViewportHeight)
		{
			this.viewportWidth = viewportWidth;
			this.viewportHeight = viewportHeight;
			lastViewportWidth = viewportWidth;
			lastViewportHeight = viewportHeight;
		}
		
	}
	
	
	
}
