package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

/**
 * @author Remi SHARROCK and Axel Mathieu
 * @description Create an actor by passing the reference of another actor at construction time.
 */
public class RequestDontWaitForResponse {

	public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create("system");
		final ActorRef a = system.actorOf(FirstActor.createActor(), "a");
		
		@SuppressWarnings("unused")
		final ActorRef b = system.actorOf(SecondActor.createActor(), "b");

		//Technically it would be nicer to have a sent the message itself, in this case actually the system will
		//sent the message and only give the reference to a as the sender
		MyMessage m1 = new MyMessage("hello Req 1");
		b.tell(m1, a);
		MyMessage m2 = new MyMessage("hello Req 2");
		b.tell(m2, a);

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
