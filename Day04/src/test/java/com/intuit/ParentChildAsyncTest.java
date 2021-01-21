package com.intuit;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import akka.actor.testkit.typed.javadsl.ActorTestKit;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;

class ParentChildAsyncTest {

	private static ActorTestKit actorTestKit;
	
	@BeforeAll
	public static void setup() {
		actorTestKit = ActorTestKit.create(); 
	}
	
	@AfterAll
	public static void tearDown() {
		actorTestKit.shutdownTestKit();
	}
	
	@Test
	void testChildHiMessage() {
		ActorRef<Command> child = actorTestKit.spawn(Child.create());
		TestProbe<String> probe = actorTestKit.createTestProbe();
		
		Command cmd = new Command();
		cmd.parent = probe.ref();
		child.tell(cmd);
		probe.expectMessage(Duration.ofSeconds(6), "hi");
		
	}
	
	@Test
	void testParentChildHiMessage() {
		ActorRef<Command> child = actorTestKit.spawn(Child.create());
		ActorRef<String> parent = actorTestKit.spawn(Parent.create());
		
		Command cmd = new Command();
		cmd.parent = parent;
		child.tell(cmd);
		
		
	}

}
