package com.intuit;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class ExceptionHandling {

	public static void main(String[] args) {

	}

}

class Parent extends AbstractBehavior<String> {

	public Parent(ActorContext<String> context) {
		super(context);
		myMathActor = context.spawn(MyMath.create(), "mymath");
	}

	private ActorRef<String> myMathActor;
	
	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("add", () -> {
					myMathActor.tell("add");
					return this;
				})
				.onMessageEquals("divide", () -> {
					myMathActor.tell("divide");
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(Parent::new);
	}
}

class MyMath extends AbstractBehavior<String> {
	
	public MyMath(ActorContext<String> context) {
		super(context);
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("add", () -> {
					int num1 = 100;
					int num2 = 100;
					System.out.println("*** Sum is " + (num1 + num2) );
					return this;
				})
				.onMessageEquals("divide", () -> {
					int num1 = 100;
					int num2 = 100;
					System.out.println("*** Division is " + (num1 / num2) );
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(MyMath::new);
	}
}
