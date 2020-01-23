package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorRef;

import java.util.ArrayList;


public class Broadcaster extends UntypedAbstractActor{

	ArrayList<ActorRef> receiver;

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Broadcaster() {
		receiver =  new ArrayList<ActorRef>();
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Broadcaster.class, () -> {
			return new Broadcaster();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageJoin){
			receiver.add(((MessageJoin) message).getMember());
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with new member");
		}

		if(message instanceof MyMessage){
			MyMessage m = (MyMessage) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to broadcast with data ["+m.data+"]");
			for (ActorRef r:receiver
				 ) {
				r.tell(m, getSender());
			}
		}
	}

}
