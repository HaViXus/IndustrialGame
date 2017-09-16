package Algorithms;

import java.util.ArrayList;
import java.util.Collections;

public class Queue<E> 
{

	private ArrayList<E> queue;
	
	public Queue()
	{
		queue = new ArrayList<E>();
	}
	public void push(E object){ queue.add(object); }
	public E pull()
	{
		if(queue.isEmpty()) return null;
		else
		{
			E object = queue.get(queue.size()-1);
			queue.remove(queue.size()-1);
			return object;
		}
	}
	public boolean isEmpty(){ return queue.isEmpty();}
	
	
}
