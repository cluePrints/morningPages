package com.mpages.hackish;

import java.lang.reflect.Field;
import java.util.Arrays;

public class Classloaders {
	public static void appendToLibraryPathsList(String pathElement) {
		Field fldUsrPaths = null;
		try {
			fldUsrPaths = ClassLoader.class.getDeclaredField("usr_paths");
			fldUsrPaths.setAccessible(true);
			String[] oldValues = (String[]) fldUsrPaths.get(null);
			String[] newValues = appendElement(oldValues, pathElement);
			fldUsrPaths.set(null, newValues);
			
		} catch (Exception ex) {
			throw new RuntimeException(ex);
			
		} finally {
			if (fldUsrPaths != null) {
				fldUsrPaths.setAccessible(false);
			}
		}
	}

	private static <T> T[] appendElement(T[] oldValues, T pathElement) {
		T[] newValues = Arrays.copyOf(oldValues, oldValues.length + 1);
		newValues[newValues.length - 1] = pathElement;
		return newValues;
	}
}
