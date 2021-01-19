package com.intuit;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class StudentBehaviorOld extends AbstractBehavior<String> {

	public StudentBehaviorOld(ActorContext<String> context) {
		super(context);
	}

	public static Behavior<String> create() {
		return Behaviors.setup(StudentBehaviorOld::new);
	}
	
	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("yesterday", () -> {
					getContext().getLog().warn("not a good way of writing code");
					System.out.println("This is how we wrote code yesterday");
					return this;
				})
				.onMessageEquals("start test", this::onStartWritingTest)
				.onMessageEquals("end test", this::onStopWritingTest)
				.build();
	}
	
	private Behavior<String> onStartWritingTest() {
		getContext().getLog().info("start test");
		System.out.println(getContext().getSelf().path() + " started writing test**");
		return this;
	}
	
	private Behavior<String> onStopWritingTest() {
		getContext().getLog().info("end test");
		System.out.println(getContext().getSelf().path() + " stopped writing test**");
		return Behaviors.stopped();
	}

}
