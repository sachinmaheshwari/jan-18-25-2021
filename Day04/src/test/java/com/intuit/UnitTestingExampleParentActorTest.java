package com.intuit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

import akka.actor.testkit.typed.javadsl.BehaviorTestKit;
import akka.actor.testkit.typed.javadsl.TestInbox;

class UnitTestingExampleParentActorTest {

	@Test
	void testParentHiMessage() {
		BehaviorTestKit<String> testKit 
			= BehaviorTestKit.create(Parent.create());
		String message = "hi";
		testKit.run(message);
		assertEquals(testKit.getAllLogEntries().size(), 1);
		assertEquals(testKit.getAllLogEntries().get(0).message(), 
				"Received hi");
	}
	
	@Test
	void testParentByeMessage() {
		BehaviorTestKit<String> testKit 
			= BehaviorTestKit.create(Parent.create());
		String message = "bye";
		testKit.run(message);
		assertEquals(testKit.getAllLogEntries().get(0).level(), Level.WARN);
	}
	
	@Test
	void testChildMessage() {
		BehaviorTestKit<Command> testKit 
			= BehaviorTestKit.create(Child.create());
		TestInbox<String> inbox = TestInbox.create();
		Command cmd = new Command();
		cmd.parent = inbox.getRef();
		
		testKit.run(cmd);
		
		assertTrue(inbox.hasMessages());
		inbox.expectMessage("hi");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
