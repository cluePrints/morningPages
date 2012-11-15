package com.mpages.haltinglogger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;

import com.google.common.base.Throwables;

/**
 * It's tricky to track all the threads around sometimes.
 * This should raise the flag when something happens not on JUnit thread and was rendered on the log.
 */
public class HaltOnWarningInLogs implements MethodRule 
{
	private Level alertLevel = Level.WARN;
	private volatile Throwable exception;
	private volatile boolean finished;
	private BlockingQueue<ILoggingEvent> alertQueue = new LinkedBlockingQueue<ILoggingEvent>();
	
	@Override
	public Statement apply(final Statement base, FrameworkMethod arg1, Object arg2) 
	{
		return new Statement() 
		{
			@Override
			public void evaluate() throws Throwable 
			{
				execute(base);
			}
		};
	}
	

	private void execute(final Statement base)
			throws InterruptedException 
	{
		Appender<ILoggingEvent> adapter = bindToLog4j();
		try
		{
			Thread thread = executeOnSeparateThread(base);
			waitForCompletionOrAlertOnLog(thread);
		}
		finally
		{
			adapter.stop();
		}
	}


	private void waitForCompletionOrAlertOnLog(Thread thread)
			throws InterruptedException 
	{
		while (!finished && exception == null)
		{
			ILoggingEvent alert = alertQueue.poll(1009, TimeUnit.MILLISECONDS);
			if (alert == null)
				continue;
			
			thread.interrupt();
			throw new IllegalStateException("Found error on log: "+ alert);
		}
		if (exception != null)
		{
			Throwables.propagate(exception);
		}
		thread.join();
	}

	private Thread executeOnSeparateThread(final Statement base) 
	{
		Thread thread= new Thread() 
		{
			@Override
			public void run() 
			{
				try 
				{
					base.evaluate();
					finished= true;
				} 
				catch (Throwable e) 
				{
					exception = e;
				}
			}
		};
		thread.setName("test main");
		thread.start();
		return thread;
	}

	
	private Appender<ILoggingEvent> bindToLog4j() 
	{
		final String logName = Logger.ROOT_LOGGER_NAME;
		ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(logName);
		Appender<ILoggingEvent> appender = new AppenderBase<ILoggingEvent>()
		{
			protected void append(ILoggingEvent eventObject) 
			{
				if (eventObject.getLevel().isGreaterOrEqual(alertLevel))
				{
					alertQueue.add(eventObject);
				}
			}
			
			@Override
			public String getName() 
			{
				return logName;
			}
		};
		appender.start();
		log.addAppender(appender);
		return appender;
	}

}
