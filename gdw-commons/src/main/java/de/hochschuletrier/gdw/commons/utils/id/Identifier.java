package de.hochschuletrier.gdw.commons.utils.id;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

/**
 * 
 * @author ElFapo
 */

public class Identifier 
{
	private Stack<Integer> 					freeIDs;
	private HashSet<Integer> 				takenIDs;
	private HashSet<Integer>				allIDs;
	
	private Integer 						idCount;
	
	public Identifier(int initialIDCount)
	{
		idCount = initialIDCount;
		
		for (int i = 1; i <= idCount; i++)
			freeIDs.push(i);
	}
	
	public Integer requestID()
	{
		if (freeIDs.isEmpty())
			freeIDs.push(++idCount);
		
		Integer id = freeIDs.pop();
		takenIDs.add(id);
		return id;
	}
	
	public void returnID(Integer id)
	{
		if (doesExist(id) && isTaken(id))
		{
			takenIDs.remove(id);
			freeIDs.push(id);
		}
	}
	
	public boolean doesExist(Integer id)
	{
		return allIDs.contains(id);
	}
	
	public boolean isTaken(Integer id)
	{
		return takenIDs.contains(id);
	}
}
