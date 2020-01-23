package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

import java.time.Duration;
import java.util.ArrayList;

public class Actor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	ArrayList<ActorRef> connections;
	int id;
	Timeout t1;

	public Actor() {
		connections = new ArrayList<>();
		id = Integer.valueOf(getSelf().path().name());
		t1 = Timeout.create(Duration.ofSeconds(5));
	}


	// Static function creating actor Props
	public static Props createActor() {
		return Props.create(Actor.class, () -> {
			return new Actor();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Topology){
			Topology top = (Topology) message;
			connections = new ArrayList<>();

			for (int i = 0; i < top.adj.length; i++) {
				if(top.adj[id][i]){
					connections.add(top.refs[i]);
				}
			}
		}

		if(message instanceof MyMessage){
			for (ActorRef c:connections
				 ) {
				c.tell(message, getSelf());
			}
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
		}
	}



}
