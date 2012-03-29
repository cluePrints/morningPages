package com.mpages.treededup;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;


public class FileSplitter {
	private long partSize;
	
	public void setPartSize(long partSize) {
		this.partSize = partSize;
	}
		
	public Collection<FilePart> split(File input) throws Exception {
		long dataLeft = input.length();
		long currentStart = 0;
		List<FilePart> parts = Lists.newLinkedList(); 
		FileInputStream is = newStream(input);		
		while (dataLeft > 0) {
			long currPartSize = Math.min(dataLeft,  partSize);
			parts.add(new FilePart(is, input.getAbsolutePath(), currentStart, currPartSize));
			currentStart += currPartSize;
			dataLeft -= currPartSize;
		}
		return parts;
	}
	
	public Collection<FilePart> splitSafely(File input) {
		try {
			return split(input);
		} catch (Exception ex) {			
			ex.printStackTrace(System.err);
			return Lists.newArrayList();
		}
	}

	FileInputStream newStream(File input) throws FileNotFoundException {
		FileInputStream is = new FileInputStream(input);
		return is;
	}
}