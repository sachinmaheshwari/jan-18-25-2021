package com.intuit;

import java.io.IOException;

import akka.actor.typed.ActorSystem;

public class ClassroomMain {

	public static void main(String[] args) throws IOException {
//		ActorSystem<String> studentActor = ActorSystem.create(StudentBehavior.create(), "sam");
//		studentActor.tell("yesterday");
//		studentActor.tell("start test");
//		System.out.println("Please hit enter to stop the test");
//		System.in.read();
//		studentActor.tell("end test");
		
		ActorSystem<String> teacherActor = ActorSystem.create(TeacherBehavior.create(), "teacher");
		teacherActor.tell("start");
		System.out.println("Please hit enter to stop the test");
		System.in.read();
		teacherActor.tell("stop");
		

	}

}
