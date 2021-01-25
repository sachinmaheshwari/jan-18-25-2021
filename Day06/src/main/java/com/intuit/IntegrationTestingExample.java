package com.intuit;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class IntegrationTestingExample {

}


class Parent extends AbstractBehavior<String> {

	public Parent(ActorContext<String> context) {
		super(context);
	}
	public ActorRef<String> child = null;
	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("greet", () -> {
					child = getContext().spawn(Child.create(), "my-child");
					System.out.println(child.path());
					child.tell("hi");
					return this;
				})
				.onMessageEquals("bye", () -> {
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(Parent::new);
	}
	
}

class Child extends AbstractBehavior<String> {

	public Child(ActorContext<String> context) {
		super(context);
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("hi", () -> {
					getContext().getLog().info("Hi there");
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(Child::new);
	}
	
}
