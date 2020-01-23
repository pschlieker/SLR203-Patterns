package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.actor.dsl.Creators;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorRef;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Caster extends UntypedAbstractActor{

	ArrayList<ActorRef> receivers;

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Caster() {
		receivers = new ArrayList<>();
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Caster.class, () -> {
			return new Caster();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageJoin){
			MessageJoin m = (MessageJoin) message;
			receivers.add(getSender());
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to add");

		}

		if(message instanceof MessageUnjoin){
			MessageUnjoin m = (MessageUnjoin) message;
			receivers.remove(getSender());
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to remove");

		}

		if(message instanceof MyMessage){
			for (ActorRef r:receivers
				 ) {
				r.tell(message, getSender());
			}
		}
	}

}
