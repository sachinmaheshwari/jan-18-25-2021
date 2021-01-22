package com.intuit.lab01;

import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

// GOOD DESIGN! BUT DOES NOT WORK :(
// MAY BE YOU CAN ASK FOR PRIME NUMBER INSTEAD OF TELL
// MAY BE INTRODUCE AN INTERMEDIATE ACTOR WHERE YOU CAN FETCH THE STATUS FROM

public class Lab01 {

	public static void main(String[] args) {
		ActorSystem<PrimeNumberCommand> mathActor = ActorSystem.create(MathBehavior.create(), "math-actor");
		mathActor.tell(new StartPrimeNumberCommand());
		//mathActor.tell(new StatusCheckPrimeNumberCommand());
	}

}

class MathBehavior extends AbstractBehavior<PrimeNumberCommand> {

	private ActorRef<PrimeNumberCommand> primeNumberActor;
	public MathBehavior(ActorContext<PrimeNumberCommand> context) {
		super(context);
		primeNumberActor = context.spawn(PrimeNumberBehavior.create(), "prime-actor", DispatcherSelector.fromConfig("my-dispatcher"));
	}

	@Override
	public Receive<PrimeNumberCommand> createReceive() {
		return newReceiveBuilder()
				.onMessage(StatusCheckPrimeNumberCommand.class, (inputCmd) -> {
					getContext().ask(StatusCommand.class, primeNumberActor, Duration.ofSeconds(1), 
							hiddenRef -> {
								StatusCheckPrimeNumberCommand cmd = new StatusCheckPrimeNumberCommand();
								cmd.ref = hiddenRef;
								return cmd;
							}, 
							(response, t) -> {
								if (response != null && response.inProgress) {
									return new InProgressCommand();
								} else {
									return new CompleteCommand();	
								}
								
							});
					return this;
				})
				.onMessage(StartPrimeNumberCommand.class, (cmd2) -> {
					for (int i = 0; i < 20; i++) {
						primeNumberActor.tell(new StartPrimeNumberCommand());
						getContext().getSelf().tell(new StatusCheckPrimeNumberCommand());	
					}
					return this;
				})
				.onMessage(InProgressCommand.class, (cmd) -> {
					System.out.println("***In progress");
					getContext().getSelf().tell(new StatusCheckPrimeNumberCommand());
					return this;
				})
				.onMessage(CompleteCommand.class, cmd -> {
					System.out.println("=====DONE");
					return Behaviors.stopped();
				})
				.build();
	}
	public static Behavior<PrimeNumberCommand> create() {
		return Behaviors.setup(MathBehavior::new);
	}
}

class PrimeNumberBehavior extends AbstractBehavior<PrimeNumberCommand> {

	private List<BigInteger> lst;
	
	public PrimeNumberBehavior(ActorContext<PrimeNumberCommand> context) {
		super(context);
		lst = new ArrayList<>();
	}

	public static Behavior<PrimeNumberCommand> create() {
		return Behaviors.setup(PrimeNumberBehavior::new);
	}
	
	@Override
	public Receive<PrimeNumberCommand> createReceive() {
		
		return newReceiveBuilder()
				.onMessage(StartPrimeNumberCommand.class, this::onStart)
				.onMessage(StatusCheckPrimeNumberCommand.class, this::onStatusCheck)
				.build();
	}
	
	private Behavior<PrimeNumberCommand> onStart(StartPrimeNumberCommand cmd) {
			BigInteger big = new BigInteger(1000, new Random());
			BigInteger prime = big.nextProbablePrime();
			lst.add(prime);
		
		return this;
	}
	
	private Behavior<PrimeNumberCommand> onStatusCheck(StatusCheckPrimeNumberCommand cmd) {
		StatusCommand statusCmd = new StatusCommand();
		if(lst.size() != 20) {
			statusCmd.inProgress = true;
		}
		else {
			statusCmd.inProgress = false;
		}
		cmd.ref.tell(statusCmd);
		return this;
	}
	
}

class PrimeNumberCommand {
	public ActorRef ref;
}

class CompleteCommand extends PrimeNumberCommand {}
class StartPrimeNumberCommand extends PrimeNumberCommand {}
class StatusCheckPrimeNumberCommand extends PrimeNumberCommand {}
class InProgressCommand extends PrimeNumberCommand {}
class StatusCommand extends PrimeNumberCommand {
	public boolean inProgress;
}