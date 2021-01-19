package com.intuit;

import akka.actor.typed.ActorRef;

public class StudentCommand {
	private ActorRef<String> teacher;
	
	public StudentCommand(ActorRef<String> teacher) {
		this.teacher = teacher;
	}
	public StudentCommand() {
	}

	public ActorRef<String> getTeacher() {
		return teacher;
	}
	public void setTeacher(ActorRef<String> teacher) {
		this.teacher = teacher;
	}
}

class StudentStartCommand extends StudentCommand {}
class StudentStopCommand extends StudentCommand {}