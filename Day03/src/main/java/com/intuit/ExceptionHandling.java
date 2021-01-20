package com.intuit;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/*
 * When an exception occurs in the Guardian actor, the application terminates
 * When an exception occurs inside a child actor the actor is terminated by default. All the messages to this actor, fall into a DLQ
 * 
 * You can configure a SupervisorStrategy for the actors. Specify what to do on failure
 * Wrap you actor's behavior with a supervisor and specify resume or restart or stop 
 * */

public class ExceptionHandling {

	public static void main(String[] args) {
		ActorSystem<String> parent = ActorSystem.create(Parent.create(), "parent");
		parent.tell("add");
		parent.tell("divide");
		parent.tell("add");
	}

}

class Parent extends AbstractBehavior<String> {

	public Parent(ActorContext<String> context) {
		super(context);
		//myMathActor = context.spawn(MyMath.create(), "mymath");
		//myMathActor = context.spawn(Behaviors.supervise(MyMath.create()).onFailure(SupervisorStrategy.resume()), "mymath");
		myMathActor = context.spawn(Behaviors.supervise(MyMath.create()).onFailure(SupervisorStrategy.restart()), "mymath");
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

	private String state = "INITIAL-101";
	
	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("add", () -> {
					state += "-ADD";
					int num1 = 100;
					int num2 = 100;
					System.out.println("*** Sum is " + (num1 + num2) + " and state is " + state);
					return this;
				})
				.onMessageEquals("divide", () -> {
					state += "-DIVIDE";
					int num1 = 100;
					int num2 = 0;
					System.out.println("*** Division is " + (num1 / num2) + " and state is " + state);
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(MyMath::new);
	}
}
