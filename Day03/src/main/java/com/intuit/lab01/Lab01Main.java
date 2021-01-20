package com.intuit.lab01;

import java.util.Arrays;


import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.Behavior;
import akka.actor.typed.SupervisorStrategy;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.PoolRouter;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Routers;

public class Lab01Main {

	public static void main(String[] args) {
		
		ActorSystem.create(Behaviors.supervise(Coordinator.create()).onFailure(SupervisorStrategy.resume()), "coord").tell(new StartCommand());
	}
}

class Coordinator extends AbstractBehavior<Command> {

	private String input = "409  194  207  470  178  454  235  333  511  103  474  293  525  372  408  428  4321  2786  6683  3921  265  262  6206  2207  5712  214  6750  2742  777  5297 3536  2675  1298  1069  175  145  706  2614  4067  4377  146  134  1930  3850 2169  1050  3705  2424  614  3253  222  3287  3340  2637  61  216  2894  247 214 99  797  80  683  789  92  736  318  103  153  749  631  626  367  110  805 2922  1764  178  3420  3246  3456  73  2668  3518  1524  273  2237  228  1826  182 4682  642  397  5208  136  4766  180  1673  1263  4757  4680  141  4430  1098  188 158  712  1382  170  550  913  191  163  459  1197  1488  1337  900  1182  1018  337  3858  202  1141  3458  2507  239  199  4400  3713  3980  4170  227  3968  1688  4352  4168  209";

	private ActorRef<Integer> sumActorRouter;
	private PoolRouter<Integer> poolRouter;
	
	public Coordinator(ActorContext<Command> context, int poolSize) {
		super(context);
	
		Behavior<Integer> sumActor = Behaviors.supervise(SumBehavior.create()).onFailure(SupervisorStrategy.resume());
		poolRouter = Routers.pool(poolSize, sumActor);
		sumActorRouter = context.spawn(poolRouter, "sum-actor-pool");
	}

	@Override
	public Receive<Command> createReceive() {
		return newReceiveBuilder()
				.onMessage(StartCommand.class, this::onStart)
				.build();
	}
	
	private Behavior<Command> onStart(StartCommand cmd) {
		Arrays.asList(input.split("  ")).forEach(it -> {
			int number = Integer.parseInt(it.trim());
			sumActorRouter.tell(number);
		});
		return this;
	}
	
	public static Behavior<Command> create() {
		int poolSize = 10; //compute dynamically
		return Behaviors.setup(ctx -> new Coordinator(ctx, poolSize));
	}
	
}

class SumBehavior extends AbstractBehavior<Integer> {

	public SumBehavior(ActorContext<Integer> context) {
		super(context);
	}

	@Override
	public Receive<Integer> createReceive() {
		return newReceiveBuilder()
				.onAnyMessage(num -> {
					System.out.println("Received " + num + " on " + getContext().getSelf().path());
					return this;
				})
				.build();
	}
	
	public static Behavior<Integer> create() {
		return Behaviors.setup(SumBehavior::new);
	}
	
}


class Command {}
class StartCommand extends Command {}