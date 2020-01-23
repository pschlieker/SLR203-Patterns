package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorRef;

import java.util.ArrayList;


public class LoadBalancer extends UntypedAbstractActor{

	ArrayList<ActorRef> receivers;
	int lastReceiver;

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public LoadBalancer() {
		receivers = new ArrayList<>();
		lastReceiver = 0;

	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(LoadBalancer.class, () -> {
			return new LoadBalancer();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageJoin){
			receivers.add(getSender());
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to add");

		}

		if(message instanceof MessageUnjoin){
			receivers.remove(getSender());
			lastReceiver = lastReceiver % receivers.size();
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to remove");

		}

		if(message instanceof MyMessage){
			receivers.get(lastReceiver).tell(message, getSender());
			lastReceiver = (lastReceiver+1) % receivers.size();
		}
	}

}
