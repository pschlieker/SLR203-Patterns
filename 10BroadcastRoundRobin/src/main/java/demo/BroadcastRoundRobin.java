package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK
 * @description
 */
public class BroadcastRoundRobin {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate first and second actor
	    final ActorRef a = system.actorOf(MyActor.createActor(), "a");
		final ActorRef b = system.actorOf(MyActor.createActor(), "b");
		final ActorRef c = system.actorOf(MyActor.createActor(), "c");
		final ActorRef broadcaster = system.actorOf(Broadcaster.createActor(), "broadcaster");

		broadcaster.tell(new MessageJoin(b), b);
		broadcaster.tell(new MessageJoin(c), c);
		broadcaster.tell(new MyMessage("Hello"), a);

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
