package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.actor.dsl.Creators;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorRef;

import java.util.ArrayList;


public class SessionManager extends UntypedAbstractActor{

	ArrayList<ActorRef> sessions;
	ArrayList<ActorRef> sessionOwners;

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public SessionManager() {
		sessions =  new ArrayList<>();
		sessionOwners = new ArrayList<>();
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(SessionManager.class, () -> {
			return new SessionManager();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageCreateSession){
			sessionOwners.add(getSender());
			ActorRef newSession = getContext().actorOf(Session.createActor(), "session");
			sessions.add(newSession);

			getSender().tell(new MessageSessionRef(newSession), getSelf());
		}

		if(message instanceof MessageEndSession){
			int idx = 0;
			for (int i = 0; i < sessionOwners.size(); i++) {
				if(sessionOwners.get(i).path().name().equalsIgnoreCase(getSender().path().name())){
					idx = i;
					break;
				}
			}
			ActorRef session = sessions.get(idx);

			sessionOwners.remove(getSender());
			sessions.remove(session);

			getContext().stop(session);
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to stop session");
		}
	}

}
