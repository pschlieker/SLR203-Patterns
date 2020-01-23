package demo;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
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
public class SearchActorWithNameOrPath {

	public static void main(String[] args) {
		final ActorSystem system = ActorSystem.create("system");
	    final ActorRef a1 = system.actorOf(FirstActor.createActor(), "a");

	    a1.tell(new Create(), a1);
	    a1.tell(new Create(), a1);

	    LinkedList<String> paths = new LinkedList<>();
	    paths.addAll(Arrays.asList(new String[]{"/*", "/user", "/system", "/deadLetters", "/temp", "/remote"}));

		while(!paths.isEmpty()) {
			String start = paths.pop();

			ActorSelection search = system.actorSelection(start);
			Timeout t1 = Timeout.create(Duration.ofSeconds(5));

			Future<ActorRef> foundActorFuture  = search.resolveOne(t1);
			ActorRef foundActor;
			try {
				foundActor = (ActorRef) Await.result(foundActorFuture, t1.duration());
				System.out.println("Found Actor with name " + foundActor.path());
				paths.add(foundActor.path().toString()+"/*");
			}catch(java.util.concurrent.TimeoutException e){
				System.out.println("No Actor found for "+start );
			} catch (akka.actor.ActorNotFound e){
				System.out.println("No Actor found for "+start );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


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
