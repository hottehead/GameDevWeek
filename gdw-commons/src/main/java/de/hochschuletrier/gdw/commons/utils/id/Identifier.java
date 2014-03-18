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
	private Stack<Long> 					freeIDs;
	private HashSet<Long> 				takenIDs;
	private HashSet<Long>				allIDs;
	
	private long 						idCount;
	
	public Identifier(long initialIDCount)
	{
		idCount = initialIDCount;
		freeIDs = new Stack<Long>();
		takenIDs = new HashSet<Long>();
		allIDs = new HashSet<Long>();
		
		for (long i = 1; i <= idCount; i++)
			freeIDs.push(i);
	}
	
	public Long requestID()
	{
		if (freeIDs.isEmpty())
			freeIDs.push(++idCount);
		
		Long id = freeIDs.pop();
		takenIDs.add(id);
		return id;
	}
	
	public void returnID(Long id)
	{
		if (doesExist(id) && isTaken(id))
		{
			takenIDs.remove(id);
			freeIDs.push(id);
		}
	}
	
	public boolean doesExist(Long id)
	{
		return allIDs.contains(id);
	}
	
	public boolean isTaken(Long id)
	{
		return takenIDs.contains(id);
	}
}
