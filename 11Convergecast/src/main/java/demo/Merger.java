package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorRef;

import java.util.ArrayList;


public class Merger extends UntypedAbstractActor{

	ArrayList<ActorRef> sender;
	ArrayList<ActorRef> senderWithMessage;
	ActorRef receiver;
	MyMessage current_message;

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Merger(ActorRef receiver) {
		sender =  new ArrayList<ActorRef>();
		senderWithMessage = new ArrayList<>();
		this.receiver = receiver;
		current_message = null;
	}

	// Static function creating actor
	public static Props createActor(ActorRef receiver) {
		return Props.create(Merger.class, () -> {
			return new Merger(receiver);
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MessageJoin){
			sender.add(((MessageJoin) message).getMember());
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with new member");
		}

		if(message instanceof MessageUnjoin){
			sender.remove(((MessageUnjoin) message).getMember());
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to remove member");
		}

		if(message instanceof MyMessage){
			MyMessage m = (MyMessage) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to broadcast with data ["+m.data+"]");

			if(current_message == null)
				current_message = m;

			if(!senderWithMessage.contains(getSender()))
				senderWithMessage.add(getSender());

			if(senderWithMessage.size() == sender.size()){
				String sendersString = "";
				for (ActorRef s:senderWithMessage) {
					sendersString += s.path().name() + " ";
				}

				receiver.tell(new MyMessageMerged(current_message.data, sendersString.trim()), getSelf());
				senderWithMessage = new ArrayList<>();
				current_message = null;
			}
		}
	}

}
