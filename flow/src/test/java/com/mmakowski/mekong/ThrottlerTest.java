package com.mmakowski.mekong;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.mmakowski.mekong.ProducerDelegate;
import com.mmakowski.mekong.Throttler;


public class ThrottlerTest {
	private ProducerDelegate<String> source;
	private Throttler<String> throttler;
	
	@Before
	public void setUp() {
		source = new ProducerDelegate<String>();
		throttler = new Throttler<String>(100);	
		source.addConsumer(throttler);
	}	

	@Test
	public void firstMessageIsNotDelayed() throws InterruptedException {
		VerifyingConsumer<String> destination = new VerifyingConsumer<String>() {
			@Override
			public synchronized void verify() {
				assertFalse(receivedMessages.isEmpty());
			}
		};
		throttler.addConsumer(destination);
		
		source.produce("testing...");
		Thread.sleep(1);
		destination.verify();
	}
	
	@Test
	public void at100mpmProduces5messagesIn49ms() throws InterruptedException {
		VerifyingConsumer<String> destination = new VerifyingConsumer<String>() {
			@Override
			public synchronized void verify() {
				assertEquals(5, receivedMessages.size());
			}
		};
		throttler.addConsumer(destination);
		
		Thread producer = new Thread() {
			@Override
			public void run() {
				long startTime = new Date().getTime();
				while (new Date().getTime() - startTime < 49) {
					source.produce("testing...");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						throw new IllegalStateException(e);
					}
				}
			}
		};
		producer.start();
		Thread.sleep(49);
		destination.verify();
	}
	
	@Test
	public void latestMessageIsPublishedAfterDelay() throws InterruptedException {
		VerifyingConsumer<String> destination = new VerifyingConsumer<String>() {
			@Override
			public synchronized void verify() {
				assertEquals("testing 3", receivedMessages.get(1));
			}
		};
		throttler.addConsumer(destination);
		
		source.produce("testing 1");
		source.produce("testing 2");
		source.produce("testing 3");
		Thread.sleep(15);
		destination.verify();
	}
}
