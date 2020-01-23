package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.time.Duration;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class ControlledFlooding {

	public static void main(String[] args) throws InterruptedException {
		//Part one with out loop!

		boolean[][] adj = new boolean[][]{
				{false, true, true, false, false},
				{false, false, false, true, false},
				{false, false, false, true, false},
				{false, false, false, false, true},
				{false, true, false, false, false}
		};

		final ActorSystem system = ActorSystem.create("system");
		ActorRef[] actors = new ActorRef[adj.length];

		for (int i = 0; i < adj.length; i++) {
			actors[i] = system.actorOf(Actor.createActor(), String.valueOf(i));
		}

		Thread.sleep(1000);

		for (int i = 0; i < adj.length; i++) {
			actors[i]. tell(new Topology(adj, actors), actors[0]);
		}

		actors[0].tell(new MyMessage(0), actors[0]);

	    // We wait 5 seconds before ending system (by default)
	    // But this is not the best solution.
	    try {
			waitBeforeTerminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
	}

	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}
}
