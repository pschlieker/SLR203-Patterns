package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK
 * @description
 */
public class Convergecast {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate first and second actor
	    final ActorRef a = system.actorOf(MyActor.createActor(), "a");
		final ActorRef b = system.actorOf(MyActor.createActor(), "b");
		final ActorRef c = system.actorOf(MyActor.createActor(), "c");
		final ActorRef d = system.actorOf(MyActor.createActor(), "d");
		final ActorRef merger = system.actorOf(Merger.createActor(d), "merger");

		merger.tell(new MessageJoin(a), a);
		merger.tell(new MessageJoin(b), b);
		merger.tell(new MessageJoin(c), c);

		merger.tell(new MyMessage("hi"), a);
		merger.tell(new MyMessage("hi"), b);
		merger.tell(new MyMessage("hi"), c);

		merger.tell(new MessageUnjoin(c), c);

		merger.tell(new MyMessage("hi"), a);
		merger.tell(new MyMessage("hi"), b);

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
