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

public class AskExample2 {

	public static void main(String[] args) {
		ActorSystem<String> parentActor = ActorSystem.create(ParentBehavior.create(), "parent");
		parentActor.tell("hello");
		
	}

}

class ParentBehavior extends AbstractBehavior<String> {

	private ActorRef<ExampleCommand> child = null;
	public ParentBehavior(ActorContext<String> context) {
		super(context);
		child = context.spawn(ChildBehavior.create(), "child");
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("fine", () -> {
					System.out.println("***Received fine from child");
					return Behaviors.stopped();
				})
				.onAnyMessage(msg -> {
					// Normal request-reponse pattern; there is no guarantee that the child will respond.
//					ActorRef<ExampleCommand> child = getContext().spawn(ChildBehavior.create(), "child");
//					ExampleCommand cmd = new ExampleCommand();
//					cmd.ref = getContext().getSelf();
//					child.tell(cmd);
					
					
					//getContext().ask(resClass, target, responseTimeout, createRequest, applyToResponse);
					getContext().ask(String.class, 
							child, 
							Duration.ofSeconds(1), 
							fabricatedHiddenActorRef -> {
								ExampleCommand cmd = new ExampleCommand();
								cmd.ref = fabricatedHiddenActorRef;
								return cmd;
							}, 
							(response, throwable) -> {
								if(response != null) {
									System.out.println("Received " + response);
								}
								else {
									System.out.println("Child is not responding yet");
								}
								return response;
							});
					
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(ParentBehavior::new);
	}
}

class ChildBehavior extends AbstractBehavior<ExampleCommand> {

	public ChildBehavior(ActorContext<ExampleCommand> context) {
		super(context);
	}

	@Override
	public Receive<ExampleCommand> createReceive() {
		return newReceiveBuilder()
				.onAnyMessage(cmd -> {
					Thread.sleep(5000);
					cmd.ref.tell("fine");
					return this;
				})
				.build();
	}
	
	public static Behavior<ExampleCommand> create() {
		return Behaviors.setup(ChildBehavior::new);
	}
}



class ExampleCommand {
	public ActorRef<String> ref;
}