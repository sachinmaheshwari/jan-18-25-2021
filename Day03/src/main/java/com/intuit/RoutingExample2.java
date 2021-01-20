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
public class RoutingExample2 {
	public static void main(String[] args) {
		PoolRouter<Object> poolRouter = Routers.pool(5, MyTranslator.create()).withConsistentHashingRouting(50, msg -> {
			int rnd = (int)Math.random() * 5;
			return msg.toString() + "_" + rnd;
		});
		ActorSystem<Object> system = ActorSystem.create(poolRouter, "mytranslator");
		
		for (int i = 0; i < 20; i++) {
			if (i % 2 == 0) {
				system.tell("hello");
			} else if (i % 2 !=0 && i < 6){
				system.tell("namaste");
			} else {
				system.tell("vanakkam");
			}
				
		}
		
		
	}
}

class MyTranslator extends AbstractBehavior<Object> {

	public MyTranslator(ActorContext<Object> context) {
		super(context);
	}
	
	
	@Override
	public Receive<Object> createReceive() {
		
		return newReceiveBuilder()
				.onMessageEquals("hello", () -> {
					System.out.println("*****hello processed by " + getContext().getSelf().path());
					return this;
				})
				.onMessageEquals("namaste", () -> {
					System.out.println("-----namaste processed by " + getContext().getSelf().path());
					return this;
				})
				.onMessageEquals("vanakkam", () -> {
					System.out.println("======vanakkam processed by " + getContext().getSelf().path());
					return this;
				})
				.build();
	}
	
	public static Behavior<Object> create() {
		return Behaviors.setup(MyTranslator::new);
	}
	
}
