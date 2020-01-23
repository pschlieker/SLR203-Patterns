package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.time.Duration;
import akka.dispatch.*;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.Await;
import scala.concurrent.Promise;
import akka.util.Timeout;
import akka.pattern.Patterns;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;

import java.util.concurrent.CompletableFuture;

public class FirstActor extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public FirstActor(ActorRef rec) {
		MyMessage m1 = new MyMessage("hello Req 1");
		Timeout t = Timeout.create(Duration.ofSeconds(5));
		Future<Object> future = Patterns.ask(rec, m1, t);
		MyMessage res1 = null;
		try {
			res1 = (MyMessage) Await.result(future, t.duration());
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+res1.data+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		MyMessage m2 = new MyMessage("hello Req 2");
		Timeout t2 = Timeout.create(Duration.ofSeconds(5));
		Future<Object> future2 = Patterns.ask(rec, m2, t2);
		MyMessage res2 = null;
		try {
			res2 = (MyMessage) Await.result(future2, t.duration());
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+res2.data+"]");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Static function creating actor Props
	public static Props createActor(ActorRef rec) {
		return Props.create(FirstActor.class, () -> {
			return new FirstActor(rec);
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof MyMessage){
			MyMessage m = (MyMessage) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
		}
	}
}
