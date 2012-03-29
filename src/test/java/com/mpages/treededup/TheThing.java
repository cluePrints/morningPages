package com.mpages.treededup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class TheThing {

	
	@Test
	public void aaa() throws Exception {
		SimpleRegistry reg = new SimpleRegistry();
		final FileSplitter fileSplitter = new FileSplitter();
		fileSplitter.setPartSize(FileUtils.ONE_MB);
		reg.put("fileSplitter", fileSplitter);
		
		DefaultCamelContext ctx = new DefaultCamelContext();
		ctx.addRoutes(new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				onException(FileNotFoundException.class)
					.to("stream:err");
				
				from("vm:treeFiles?concurrentConsumers=3")
					.split().method(fileSplitter,  "splitSafely")
					.bean(HashCalculator.class, "calc")					
					.to("file://out.txt&append=true&autoCreate=true");
							
			}
		});
		
		final ProducerTemplate producer = ctx.createProducerTemplate();		
		FileScanner.Callback<File> resultCallback = new FileScanner.Callback<File>() {
			public void call(File t) {
				producer.sendBody("vm:treeFiles", t.toString());
			}
		};
		
		long start = System.currentTimeMillis();
		ctx.start();
		
		FileScanner scanner = new FileScanner();
		scanner.scan("/home/user", resultCallback);
		
		ctx.stop();
		long timeMs = System.currentTimeMillis() - start;
		System.out.println("== Time spent: "+timeMs);
	}
}
