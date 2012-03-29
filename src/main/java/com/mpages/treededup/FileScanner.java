package com.mpages.treededup;
import java.io.File;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;


public class FileScanner {	
	public void scan(String path, Callback<File> resultCallback) {
		Queue<File> directoriesToScan = new LinkedBlockingDeque<File>();
		File initial = new File(path);
		if (!initial.isDirectory())
			throw new IllegalStateException();
		
		directoriesToScan.add(initial);
		scanInternal(directoriesToScan, resultCallback);
	}

	private void scanInternal(Queue<File> directoriesToScan, Callback<File> resultCallback) {
		File current = directoriesToScan.remove();
		
		File[] files = current.listFiles();
		if (files == null)			
			return;
		
		for (File f : files) {
			if (f.isDirectory()) {
				directoriesToScan.add(f);
				scanInternal(directoriesToScan, resultCallback);
			} else {
				resultCallback.call(f);
			}
		}
	}
	
	public interface Callback<T>{
		void call(T t);
	}
}
