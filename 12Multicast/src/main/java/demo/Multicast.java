package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK
 * @description
 */
public class Multicast {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate first and second actor
	    final ActorRef r1 = system.actorOf(MyActor.createActor(), "Receiver1");
		final ActorRef r2 = system.actorOf(MyActor.createActor(), "Receiver2");
		final ActorRef r3 = system.actorOf(MyActor.createActor(), "Receiver3");
		final ActorRef s = system.actorOf(MyActor.createActor(), "Sender");
		final ActorRef c = system.actorOf(Caster.createActor(), "Caster");


		c.tell(new MessageJoin("group1", new ActorRef[]{r1,r2}), s);
		c.tell(new MessageJoin("group2", new ActorRef[]{r2,r3}), s);

		c.tell(new MyMessageGroup("hello", "group1"), s);
		c.tell(new MyMessageGroup("world", "group2"), s);


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
