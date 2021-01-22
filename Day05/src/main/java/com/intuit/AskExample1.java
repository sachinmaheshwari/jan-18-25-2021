package com.intuit;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.CompletionStage;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.AskPattern;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class AskExample1 {

	public static void main(String[] args) {
		ActorSystem<Command> timeActor = ActorSystem.create(TimeBehavior.create(), "time-behavior");
		
		CompletionStage<String> completionStage = AskPattern.ask(timeActor, 
				fabricatedHiddenActorRef -> {
					Command cmd = new Command();
					cmd.ref = fabricatedHiddenActorRef;
					return cmd;
				}, 
				Duration.ofSeconds(2), 
				timeActor.scheduler());
		
		completionStage.whenComplete((response, throwable) -> {
			if(response != null) {
				System.out.println(response);
			}
			else {
				System.out.println("Oops! no response");
			}
		});
		System.out.println("End of main");
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
					Thread.sleep(5000);
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