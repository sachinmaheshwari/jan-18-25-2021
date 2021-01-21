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
					getContext().getLog().debug("Received hi" );
					return this;
				})
				.onMessageEquals("bye", () -> {
					getContext().getLog().warn("Received bye" );
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
		return newReceiveBuilder()
				.onAnyMessage(cmd -> {
					cmd.parent.tell("hi");
					return this;
				})
				.build();
	}
	public static Behavior<Command> create() {
		return Behaviors.setup(Child::new);
	}
	
}

class Command {
	public ActorRef<String> parent;
}
