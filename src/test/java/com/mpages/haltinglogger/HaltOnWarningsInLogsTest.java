package com.mpages.haltinglogger;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HaltOnWarningsInLogsTest 
{
	@Rule
	public HaltOnWarningInLogs failOnWarningInLogs = new HaltOnWarningInLogs();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private static final Logger log = LoggerFactory.getLogger(Logger.class);
	
	@Test
	public void shouldThrowAnExceptionOnAnyWarningInTheLogs()
	{
		expectedException.expect(IllegalStateException.class);
		expectedException.expectMessage(Matchers.containsString("Logged message"));
		log.warn("Logged message");
	}
	
	@Test
	public void shouldThrowAnExceptionOnAnyWarningInTheLogsEvenFromOtherThread() throws Exception
	{
		expectedException.expect(IllegalStateException.class);
		Thread t = new Thread()
		{
			@Override
			public void run() 
			{
				log.warn("Message logged from another thread");
			}
		};
		t.start();
		t.join();
	}
	
	@Test
	public void shouldThowNoExceptionsIfNoWarnings() throws Exception
	{
		log.info("WARNing, there's no warnings here");
	}
}
