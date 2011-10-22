package com.mpages.trees;

import java.util.Comparator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class TreeTest {
	Comparator<Integer> c = new Comparator<Integer>() {
		public int compare(Integer o1, Integer o2) {
			return o1-o2;
		}
	};
	BinaryTreeMap<Integer, String> unit;
	
	@Before
	public void before()
	{
		unit = new BinaryTreeMap<Integer, String>(c);
	}
	
	@Test
	public void shouldReturnOldValue(){
		unit.put(2, "test2");
		unit.put(1, "test1");		
		unit.put(3, "test3");
		String t2 = unit.put(2, "test22");
		Assert.assertEquals("test2", t2);
		Assert.assertEquals("test3", unit.put(3, "test33"));
		Assert.assertEquals("test1", unit.put(1, "test11"));
	}
	
	@Test
	public void shouldReplaceValueIfSameKeyUsedTwice(){
		unit.put(2, "test2");
		String t2 = unit.put(2, "test22");
		Assert.assertEquals("test2", t2);
		
		Assert.assertEquals("test22", unit.put(2, "test2"));
	}
	
	@Test
	public void shouldRemoveValsOnClear(){
		unit.put(2, "test2");
		String t2 = unit.put(2, "test22");
		Assert.assertEquals("test2", t2);
		
		unit.clear();
		
		String t22 = unit.put(2, "test22");
		Assert.assertNull(t22);
	}
	
	@Test
	public void shouldSetSizeTo0OnOnClear(){
		unit.put(2, "test2");
		String t2 = unit.put(2, "test22");
		Assert.assertEquals("test2", t2);
		
		Assert.assertEquals(1, unit.size());
		
		unit.clear();
		
		Assert.assertEquals(0, unit.size());
		Assert.assertTrue(unit.isEmpty());
	}
	
	@Test
	public void shouldNotChangeSizeOnAddingValueWithSameKey(){
		unit.put(2, "test2");
		unit.put(2, "test22");
		
		Assert.assertEquals(1, unit.size());
		
		unit.put(2, "test22");
		
		Assert.assertEquals(1, unit.size());
	}
	
	@Test
	public void shouldGetValuesSaved(){
		unit.put(2, "test2");
		unit.put(1, "test1");		
		unit.put(3, "test3");
		Assert.assertEquals("test2", unit.get(2));
		Assert.assertEquals("test3", unit.get(3));
		Assert.assertEquals("test1", unit.get(1));
	}
	
	@Test
	public void shouldNotGetValuesNotPut(){
		unit.put(2, "test2");
		unit.put(1, "test1");		
		unit.put(3, "test3");
		Assert.assertNull(unit.get(11));
	}
	
	@Test
	public void shouldContainKeysPutThere(){
		unit.put(2, "test2");
		unit.put(1, "test1");		
		unit.put(3, "test3");
		Assert.assertTrue(unit.containsKey(2));
		Assert.assertFalse(unit.containsKey(-1));
		Assert.assertFalse(unit.containsKey(11));
	}
	
	@Test
	public void shouldReturnNullForEmptyMapGetting() {
		Assert.assertNull(unit.get(11));
	}
	
	@Test
	public void shouldBeCreatedEmpty() {
		Assert.assertTrue(unit.isEmpty());
	}
	
	@Test
	public void shouldBeOkToRemoveValsFromEmptyMap(){
		Assert.assertNull(unit.remove(1));
	}
	
	@Test
	public void shouldRemoveValsFromRootOnlyMap(){
		unit.put(1, "txt");
		Assert.assertEquals("txt", unit.remove(1));
		Assert.assertTrue(unit.isEmpty());
	}
	
	@Test
	public void shouldRemoveRootFromMap(){
		unit.put(2, "2");
		unit.put(3, "3");
		unit.put(1, "1");
		Assert.assertEquals("2", unit.remove(2));
		Assert.assertEquals(2, unit.size());
		Assert.assertEquals("1", unit.remove(1));
		Assert.assertEquals(null, unit.remove(11));
		Assert.assertEquals("3", unit.remove(3));
	}
	
	@Test
	public void shouldRemoveRootFromMapOfHeight2(){
		testCase(2, 1, 5, -1, 0, 3, 6);
		checkMap(2, 1, 5, -1, 0, 3, 6);
		
		unit.remove(2);
		
		Assert.assertNull(unit.get(2));
		checkMap(1, 5, -1, 0, 3, 6);
	}
	
	@Test
	public void shouldMiddleFromMapOfHeight2(){
		testCase(2, 1, 5, -1, 0, 3, 6);
		checkMap(2, 1, 5, -1, 0, 3, 6);
		
		unit.remove(1);
		
		Assert.assertNull(unit.get(1));
		checkMap(2, 5, -1, 0, 3, 6);
	}
	
	@Test
	public void shouldLeafFromMapOfHeight2(){
		testCase(2, 1, 5, -1, 0, 3, 6);
		checkMap(2, 1, 5, -1, 0, 3, 6);
		
		unit.remove(0);
		
		Assert.assertNull(unit.get(0));
		checkMap(2, 1, 5, -1, 3, 6);
	}
	
	private void testCase(int... vals) {
		for (int val : vals) {
			unit.put(val, String.valueOf(val));
		}
	}
	
	private void checkMap(int... vals) {
		for (int val : vals) {
			Assert.assertEquals(String.valueOf(val), unit.get(val));
		}
	}
}
