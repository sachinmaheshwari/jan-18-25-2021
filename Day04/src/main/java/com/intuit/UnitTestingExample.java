package com.intuit;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class UnitTestingExample {

}

class Parent extends AbstractBehavior<String> {

	public Parent(ActorContext<String> context) {
		super(context);
	}
	
	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("hi", () -> {
					System.out.println("Received hi" );
					return this;
				})
				.onMessageEquals("bye", () -> {
					System.out.println("Received bye" );
					return Behaviors.stopped();
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(Parent::new);
	}
}

class Child extends AbstractBehavior<Command> {

	public Child(ActorContext<Command> context) {
		super(context);
	}

	@Override
	public Receive<Command> createReceive() {
		return null;
	}
	
}

class Command {
	public ActorRef<String> parent;
}
