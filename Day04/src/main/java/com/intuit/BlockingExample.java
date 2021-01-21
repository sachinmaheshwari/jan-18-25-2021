package com.intuit;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.DispatcherSelector;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class BlockingExample {

	public static void main(String[] args) {	
		ActorSystem<String> guardian = ActorSystem.create(GuardianBehavior.create(), "guardian");
		guardian.tell("start");
	}

}

class GuardianBehavior extends AbstractBehavior<String> {

	public GuardianBehavior(ActorContext<String> context) {
		super(context);
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onAnyMessage(msg -> {
					for (int i = 1; i <= 100; i++) {
						getContext().spawn(FastPrinter.create(), "fast-" + i).tell(i + "");
						//getContext().spawn(SlowPrinter.create(), "slow-" + i, DispatcherSelector.blocking()).tell(i + "");
						getContext().spawn(SlowPrinter.create(), "slow-" + i, DispatcherSelector.fromConfig("slow-printer-dispatcher")).tell(i + "");
					}
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(GuardianBehavior::new);
	}
	
}

class FastPrinter extends AbstractBehavior<String> {
	
	public FastPrinter(ActorContext<String> context) {
		super(context);
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onAnyMessage(msg -> {
					System.out.println("====Fast printing " + msg);
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(FastPrinter::new);
	}
	
} 


class SlowPrinter extends AbstractBehavior<String> {
	
	public SlowPrinter(ActorContext<String> context) {
		super(context);
	}

	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onAnyMessage(msg -> {
					Thread.sleep(5000);
					System.out.println("====Slow printing " + msg);
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(SlowPrinter::new);
	}
	
} 
