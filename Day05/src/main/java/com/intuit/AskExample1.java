package com.intuit;

import java.time.LocalDateTime;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class AskExample1 {

	public static void main(String[] args) {

	}

}

class TimeBehavior extends AbstractBehavior<Command> {

	public TimeBehavior(ActorContext<Command> context) {
		super(context);
	}

	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder()
				.onAnyMessage(cmd -> {
					cmd.ref.tell("It's " + LocalDateTime.now());
					return this;
				})
				.build();
	}
	
	public static Behavior<Command> create() {
		return Behaviors.setup(TimeBehavior::new);
	}
}

class Command {
	public ActorRef<String> ref;
}