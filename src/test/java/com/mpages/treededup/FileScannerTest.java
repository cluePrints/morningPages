package com.mpages.treededup;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.junit.Test;


public class FileScannerTest {
	@Test
	public void shouldScanTheTree() {
		final AtomicInteger counter = new AtomicInteger(0);
		FileScanner.Callback<File> callback = new FileScanner.Callback<File>() {
			public void call(File t) {
				counter.incrementAndGet();
				Assert.assertTrue(t.isFile());
			}
		};
		FileScanner scanner = new FileScanner();
		scanner.scan("src/test/resources", callback);
		
		Assert.assertEquals(4, counter.get());		
	}
}
