package com.mpages.screener;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.common.base.Throwables;

public class FileLinesIterator implements Iterator<String>, Closeable
{
	private final BufferedReader reader;
	private String nextLine;
	
	public FileLinesIterator(String path)
	{
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
		}
		catch (Exception ex)
		{
			throw new RuntimeException();
		}
	}
	
	@Override
	public boolean hasNext() 
	{
		readNextLineIfNecessary();
		return nextLine != null;
	}
	
	@Override
	public String next() 
	{
		readNextLineIfNecessary();
		if (!hasNext())
			throw new NoSuchElementException();
		
		String valueToReturn = nextLine;
		nextLine = null;
		return valueToReturn; 
	}
	
	@Override
	public void remove() 
	{
		throw new IllegalStateException("Not supported");
	}

	private void readNextLineIfNecessary()  
	{
		try
		{
			if (nextLine == null)
			{
				nextLine = reader.readLine();
			}
		}
		catch (IOException ex)
		{
			Throwables.propagate(ex);
		}
	}
	
	@Override
	public void close() throws IOException 
	{
		reader.close();
	}
}
