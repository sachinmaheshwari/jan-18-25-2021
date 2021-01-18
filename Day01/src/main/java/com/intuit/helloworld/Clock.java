package com.intuit.helloworld;

import java.time.LocalDateTime;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

// class Clock extends AbstractActor in classic version
public class Clock extends AbstractBehavior<String> {

	public Clock(ActorContext<String> context) {
		super(context);
	}
	
	//simple factory method to create this actor instance
	public static Behavior<String> create() {
		//return Behaviors.setup(ctx -> new Clock(ctx));
		return Behaviors.setup(Clock::new);
	}

	//Actual logic of doing something is defined in this method
	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("now", () -> {
					System.out.println(Thread.currentThread().getName());
					System.out.println("It's " + LocalDateTime.now());
					return this;
				})
				.build();
	}
	
}
