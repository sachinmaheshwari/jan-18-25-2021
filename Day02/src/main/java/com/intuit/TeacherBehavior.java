package com.intuit;

import java.util.List;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Behaviors;

public class TeacherBehavior extends AbstractBehavior<String> {

	public TeacherBehavior(ActorContext<String> context) {
		super(context);
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(TeacherBehavior::new);
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("finished", this::onFinished)
				.onMessageEquals("start", this::onStart)
				.onMessageEquals("stop", this::onStop)
				.build();
	}
	
	private ActorRef<StudentCommand> studentActor = null;
	
	private Behavior<String> onFinished() {
		System.out.println("************* " + getContext().getChild("student1").get().path() + " has already finisihed********");
		return Behaviors.stopped();
	}
	
	private Behavior<String> onStart() {
		// I am gonna ask the student to start writing the test
		
		for (int i = 1; i < 11; i++) {
			ActorRef<StudentCommand> student = getContext().spawn(StudentBehavior.create(), "student-" + i);
			StudentStartCommand cmd = new StudentStartCommand();
			cmd.setTeacher(getContext().getSelf());
			student.tell(cmd);
		}
		
//		studentActor = getContext().spawn(StudentBehavior.create(), "student1");
//		StudentStartCommand cmd = new StudentStartCommand();
//		cmd.setTeacher(getContext().getSelf());
//		
//		studentActor.tell(cmd);
		return this;
	}
	
	private Behavior<String> onStop() {
		// I am gonna ask the student to stop writing the test
	
		for (int i = 1; i < 11; i++) {
			ActorRef actor = getContext().getChild("student-" + i).get();
			StudentStopCommand cmd = new StudentStopCommand();
			cmd.setTeacher(getContext().getSelf());
			actor.tell(cmd);
		}
		
//		.forEach(child -> {
//			StudentStopCommand cmd = new StudentStopCommand();
//			cmd.setTeacher(getContext().getSelf());
//			child.tell(cmd);	
//		});
//		StudentStopCommand cmd = new StudentStopCommand();
//		cmd.setTeacher(getContext().getSelf());
//
//		studentActor.tell(cmd);
		return Behaviors.stopped();
	}
	
}
