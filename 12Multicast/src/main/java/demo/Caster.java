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


	class Group{
		String name;
		ArrayList<ActorRef> receivers;

		public Group(String name){
			this.name = name;
			this.receivers = new ArrayList<>();
		}

		public void addReceiver(ActorRef r){
			this.receivers.add(r);
		}

		public void addReceiver(ActorRef[] rs){
			for (ActorRef r:rs
				 ) {
				receivers.add(r);
			}
		}

		public void sentMessage(String m, ActorRef sender){
			for (ActorRef r:receivers
				 ) {
				r.tell(new MyMessage(m), sender);
			}
		}
	}

	ArrayList<Group> groups;

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Caster() {
		groups = new ArrayList<>();
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Caster.class, () -> {
			return new Caster();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MyMessageGroup){
			MyMessageGroup m = (MyMessageGroup) message;
			for (Group g:
				 groups) {
				if (g.name.equalsIgnoreCase(m.receivers)){
					g.sentMessage(m.data, getSender());
				}
			}

			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to broadcast to group ["+m.receivers+"] with data ["+m.data+"]");

		}

		if(message instanceof MessageJoin){

			MessageJoin m = (MessageJoin) message;
			for (Group g:
					groups) {
				if (g.name.equalsIgnoreCase(m.group)){
					g.addReceiver(m.join);
					return;
				}
			}

			Group g = new Group(m.group);
			g.addReceiver(m.join);

			groups.add(g);

			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to add to group ["+m.group+"]");
		}
	}

}
