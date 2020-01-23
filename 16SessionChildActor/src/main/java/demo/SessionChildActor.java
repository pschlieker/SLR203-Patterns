package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK
 * @description
 */
public class SessionChildActor {

	public static void main(String[] args) {

		final ActorSystem system = ActorSystem.create("system");
		
		// Instantiate first and second actor
		final ActorRef sessionManager = system.actorOf(SessionManager.createActor(), "sessionManager");
		final ActorRef client1 = system.actorOf(MyActor.createActor(sessionManager), "client1");

		sessionManager.tell(new MessageCreateSession(), client1);

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
