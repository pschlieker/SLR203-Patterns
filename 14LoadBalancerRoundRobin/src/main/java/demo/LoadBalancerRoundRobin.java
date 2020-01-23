package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK
 * @description
 */
public class LoadBalancerRoundRobin {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate first and second actor
	    final ActorRef a = system.actorOf(MyActor.createActor(), "a");
		final ActorRef b = system.actorOf(MyActor.createActor(), "b");
		final ActorRef c = system.actorOf(MyActor.createActor(), "c");
		final ActorRef lb = system.actorOf(LoadBalancer.createActor(), "loadbalancer");

		lb.tell(new MessageJoin(), b);
		lb.tell(new MessageJoin(), c);

		lb.tell(new MyMessage("m1"), a);
		lb.tell(new MyMessage("m2"), a);
		lb.tell(new MyMessage("m3"), a);

		lb.tell(new MessageUnjoin(), c);

		lb.tell(new MyMessage("m4"), a);

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
