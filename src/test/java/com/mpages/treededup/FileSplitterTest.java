package com.mpages.treededup;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Iterables;


public class FileSplitterTest {
	FileSplitter unit = new FileSplitter(){
		FileInputStream newStream(File input) throws FileNotFoundException {return null;};
	};
	
	@Test
	public void shouldSplitFile() throws Exception{
		File f = EasyMock.createMock(File.class);
		EasyMock.expect(f.getAbsolutePath()).andReturn("").anyTimes();
		EasyMock.expect(f.length()).andReturn(4L);
		EasyMock.replay(f);
		
		unit.setPartSize(3);
		Collection<FilePart> res = unit.split(f);
		
		FilePart p1 = Iterables.get(res, 0);		
		FilePart p2 = Iterables.get(res, 1);
		
		Assert.assertEquals(new FilePart(null, "", 0, 3), p1);
		Assert.assertEquals(new FilePart(null, "", 3, 1), p2);
	}
	
	@Test
	public void shouldSurviveZeroSizeFiles() throws Exception {
		File f = EasyMock.createMock(File.class);
		EasyMock.expect(f.getAbsolutePath()).andReturn("");
		EasyMock.expect(f.length()).andReturn(0L);
		EasyMock.replay(f);
		
		unit.setPartSize(3);
		Collection<FilePart> res = unit.split(f);
		
		Assert.assertEquals(0, res.size());
	}
	
	@Test
	public void shouldNotSplitFilesSmallerThanPiece() throws Exception {
		File f = EasyMock.createMock(File.class);
		EasyMock.expect(f.getAbsolutePath()).andReturn("");
		EasyMock.expect(f.length()).andReturn(2L);
		EasyMock.replay(f);
		
		unit.setPartSize(3);
		Collection<FilePart> res = unit.split(f);
		
		Assert.assertEquals(1, res.size());
		FilePart p1 = Iterables.get(res, 0);		
		
		Assert.assertEquals(new FilePart(null, "", 0, 2), p1);
		
	}
}
