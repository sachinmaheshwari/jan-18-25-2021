package com.intuit;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.PoolRouter;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Routers;

/*
 * Actors always process messages sequentially; because the messages are queued
 * Actors cannot consume more than 1 message at a time
 * 
 * 
 * */
public class RoutingExample1 {
	public static void main(String[] args) {
//		ActorSystem<Object> printer = ActorSystem.create(Printer.create(), "printer1");
//		for (int i = 0; i < 10; i++) {
//			printer.tell("invoke");
//		}
		
		//Define a router and specify a pool of actors and pass the message to the router
		PoolRouter<Object> router = Routers.pool(3, Printer.create()).withRandomRouting();//.withRoundRobinRouting();
		ActorSystem<Object> system = ActorSystem.create(router, "printer-pool");
		for (int i = 0; i < 10; i++) {
			system.tell("invoke");
		}
		
		
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
