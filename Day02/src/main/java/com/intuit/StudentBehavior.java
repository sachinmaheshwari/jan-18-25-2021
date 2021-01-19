package com.intuit;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class StudentBehavior extends AbstractBehavior<StudentCommand> {

	public StudentBehavior(ActorContext<StudentCommand> context) {
		super(context);
	}

	public static Behavior<StudentCommand> create() {
		return Behaviors.setup(StudentBehavior::new);
	}
	
	@Override
	public Receive<StudentCommand> createReceive() {
		return newReceiveBuilder()
				.onMessage(StudentStartCommand.class, this::onStartWritingTest)
				.onMessage(StudentStopCommand.class, this::onStopWritingTest)
				.build();
	}
	
	private Behavior<StudentCommand> onStartWritingTest(StudentCommand cmd) {
		getContext().getLog().info("start test");
		System.out.println(getContext().getSelf().path() + " started writing test**");
		cmd.getTeacher().tell("finished");
		return Behaviors.stopped();
	}
	
	private Behavior<StudentCommand> onStopWritingTest(StudentCommand cmd) {
		getContext().getLog().info("end test");
		System.out.println(getContext().getSelf().path() + " stopped writing test**");
		return Behaviors.stopped();
	}

}
