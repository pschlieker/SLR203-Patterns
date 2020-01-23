package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK
 * @description
 */
public class PublishSubscribe {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate first and second actor
	    final ActorRef a = system.actorOf(MyActor.createActor(), "a");
		final ActorRef b = system.actorOf(MyActor.createActor(), "b");
		final ActorRef c = system.actorOf(MyActor.createActor(), "c");
		final ActorRef p1 = system.actorOf(MyActor.createActor(), "p1");
		final ActorRef p2 = system.actorOf(MyActor.createActor(), "p2");
		final ActorRef t1 = system.actorOf(Caster.createActor(), "t1");
		final ActorRef t2 = system.actorOf(Caster.createActor(), "t2");

		t1.tell(new MessageJoin(), a);
		t1.tell(new MessageJoin(), b);
		t2.tell(new MessageJoin(), b);
		t2.tell(new MessageJoin(), c);

		t1.tell(new MyMessage("hello"), p1);
		t2.tell(new MyMessage("world"), p2);

		t1.tell(new MessageUnjoin(), a);
		t1.tell(new MyMessage("hello2"), p1);


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
