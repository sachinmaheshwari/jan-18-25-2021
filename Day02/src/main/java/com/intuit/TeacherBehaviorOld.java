package com.intuit;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Behaviors;

public class TeacherBehaviorOld extends AbstractBehavior<String> {

	public TeacherBehaviorOld(ActorContext<String> context) {
		super(context);
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(TeacherBehaviorOld::new);
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("start", this::onStart)
				.onMessageEquals("stop", this::onStop)
				.build();
	}
	
	private ActorRef<String> studentActor = null;
	
	private Behavior<String> onStart() {
		// I am gonna ask the student to start writing the test
		studentActor = getContext().spawn(StudentBehaviorOld.create(), "student1");
		studentActor.tell("start test");
		return this;
	}
	
	private Behavior<String> onStop() {
		// I am gonna ask the student to stop writing the test
		studentActor.tell("end test");
		return Behaviors.stopped();
	}
	
}
