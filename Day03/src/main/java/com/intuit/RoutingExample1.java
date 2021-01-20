package com.intuit;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class RoutingExample1 {

	public static void main(String[] args) {

	}

}

class Printer extends AbstractBehavior<Object> {

	public Printer(ActorContext<Object> context) {
		super(context);
	}
	private int invocationCount = 0;
	@Override
	public Receive<Object> createReceive() {
		
		return newReceiveBuilder()
				.onMessageEquals("invoke", () -> {
					invocationCount++;
					System.out.println("****" + getContext().getSelf().path() + " invoked and count is (" + invocationCount + ")");
					return this;
				})
				.build();
	}
	
	public static Behavior<Object> create() {
		return Behaviors.setup(Printer::new);
	}
	
}
