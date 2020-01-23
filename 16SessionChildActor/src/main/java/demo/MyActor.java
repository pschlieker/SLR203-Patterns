package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class MyActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	ActorRef session;
	ActorRef sessionManager;

	public MyActor(ActorRef sessionManager) {
		this.sessionManager = sessionManager;
	}

	// Static function creating actor
	public static Props createActor(ActorRef sessionManager) {
		return Props.create(MyActor.class, () -> {
			return new MyActor(sessionManager);
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MyMessage){
			MyMessage m = (MyMessage) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");

			if(m.data.equalsIgnoreCase("m2")){
				session.tell(new MyMessage("m3"), getSelf());
				sessionManager.tell(new MessageEndSession(), getSender());
			}
		}

		if(message instanceof MessageSessionRef){
			MessageSessionRef r = (MessageSessionRef) message;
			session = r.ref;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with reference to session");

			session.tell(new MyMessage("m1"), getSelf());
		}
	}

}
