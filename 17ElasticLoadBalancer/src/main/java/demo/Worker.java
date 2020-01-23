package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import java.util.LinkedList;

public class Worker extends UntypedAbstractActor{

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	LinkedList<String> tasks;
	Thread workerThread;
	ActorRef owner;

	public Worker() {
		tasks = new LinkedList<>();
		workerThread = null;
	}

	// Static function creating actor
	public static Props createActor() {
		return Props.create(Worker.class, () -> {
			return new Worker();
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Task){
			Task m = (Task) message;
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.data+"]");
			tasks.add(m.data);
			owner = getSender();

			if(workerThread == null){
				workerThread = new Thread(new Runnable() {
					@Override
					public void run() {
						while(tasks.size() != 0){
							String task = tasks.pop();
							log.info("Doing task "+task);
							try {
								if(task.equalsIgnoreCase("t2")){
									Thread.sleep(5000);
								}
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							owner.tell(new TaskFinished(task+" finished"), getSelf());
						}
					}
				});
				workerThread.start();
			}

		}
	}

}
