package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;


/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class RequestBlockWaitForResponseASK {

	public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create("system");
		final ActorRef b = system.actorOf(SecondActor.createActor(), "b");
		
		@SuppressWarnings("unused")
		final ActorRef a = system.actorOf(FirstActor.createActor(b), "a");

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
