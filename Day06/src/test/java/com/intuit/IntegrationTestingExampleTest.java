package com.intuit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import akka.actor.testkit.typed.javadsl.BehaviorTestKit;
import akka.actor.testkit.typed.javadsl.TestInbox;
import akka.actor.typed.ActorRef;

class IntegrationTestingExampleTest {

	@Test
	void testPassingOfMessages() {
		BehaviorTestKit<String> parent = BehaviorTestKit.create(Parent.create(), "parent");
		parent.run("greet");
		TestInbox<String> childActor = parent.childInbox("my-child");
		assertNotNull(childActor);
		childActor.expectMessage("hi");
		TestInbox<String> parentInbox = parent.selfInbox();
		parentInbox.expectMessage("bye"); //Fails
	}

}
