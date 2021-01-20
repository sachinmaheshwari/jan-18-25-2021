package com.intuit;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

/*
 * When an exception occurs in the Guardian actor, the application terminates
 * When an exception occurs inside a child actor the actor is terminated by default. All the messages to this actor, fall into a DLQ
 * 
 * You can configure a SupervisorStrategy for the actors. Specify what to do on failure
 * Wrap you actor's behavior with a supervisor and specify resume or restart or stop 
 * */

public class ExceptionHandling {

	String input = "409  194  207  470  178  454  235  333  511  103  474  293  525  372  408  428 4321  2786  6683  3921  265  262  6206  2207  5712  214  6750  2742  777  5297 3536  2675  1298  1069  175  145  706  2614  4067  4377  146  134  1930  3850 2169  1050  3705  2424  614  3253  222  3287  3340  2637  61  216  2894  247 214 99  797  80  683  789  92  736  318  103  153  749  631  626  367  110  805 2922  1764  178  3420  3246  3456  73  2668  3518  1524  273  2237  228  1826  182 4682  642  397  5208  136  4766  180  1673  1263  4757  4680  141  4430  1098  188 158  712  1382  170  550  913  191  163  459  1197  1488  1337  900  1182  1018  337 3858  202  1141  3458  2507  239  199  4400  3713  3980  4170  227  3968  1688  4352 4168 209";
	public static void main(String[] args) {
		ActorSystem<String> parent = ActorSystem.create(Parent.create(), "parent");
		parent.tell("add");
		parent.tell("divide");
		parent.tell("add");
	}

}

class Parent extends AbstractBehavior<String> {

	public Parent(ActorContext<String> context) {
		super(context);
		//myMathActor = context.spawn(MyMath.create(), "mymath");
		//myMathActor = context.spawn(Behaviors.supervise(MyMath.create()).onFailure(SupervisorStrategy.resume()), "mymath");
		myMathActor = context.spawn(Behaviors.supervise(MyMath.create()).onFailure(SupervisorStrategy.restart()), "mymath");
	}

	private ActorRef<String> myMathActor;
	
	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("add", () -> {
					myMathActor.tell("add");
					return this;
				})
				.onMessageEquals("divide", () -> {
					myMathActor.tell("divide");
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(Parent::new);
	}
}

class MyMath extends AbstractBehavior<String> {
	
	public MyMath(ActorContext<String> context) {
		super(context);
	}

	private String state = "INITIAL-101";
	
	@Override
	public Receive<String> createReceive() {
		return newReceiveBuilder()
				.onMessageEquals("add", () -> {
					state += "-ADD";
					int num1 = 100;
					int num2 = 100;
					System.out.println("*** Sum is " + (num1 + num2) + " and state is " + state);
					return this;
				})
				.onMessageEquals("divide", () -> {
					state += "-DIVIDE";
					int num1 = 100;
					int num2 = 0;
					System.out.println("*** Division is " + (num1 / num2) + " and state is " + state);
					return this;
				})
				.build();
	}
	
	public static Behavior<String> create() {
		return Behaviors.setup(MyMath::new);
	}
}
