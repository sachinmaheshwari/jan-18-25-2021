package com.intuit;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.Behaviors;

public class FuncStyleExample {

	public static void main(String[] args) {
		
		Behavior<String> helloWorld = Behaviors.setup(context -> {
			return Behaviors
					.receive(String.class)
					.onMessageEquals("hello", () -> {
						context.getLog().info("Hello there");
						return Behaviors.same();
					})
					.onMessageEquals("bye", () -> {
						context.getLog().info("Bye Bye");
						return Behaviors.stopped();
					})
					.build();
		});
		
		ActorSystem<String> actor = ActorSystem.create(helloWorld, "hw");
		actor.tell("hello");
		actor.tell("bye");
		
		

	}

}
