package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.actor.ActorRef;

import java.util.ArrayList;


public class LoadBalancer extends UntypedAbstractActor{

	private class WorkerWrapper{
		int tasks;
		ActorRef ref;

		public WorkerWrapper(int id){
			ref = getContext().actorOf(Worker.createActor(), "worker"+id);
			tasks = 0;
		}

		public void doTask(Task m){
			ref.tell(m, getSelf());
			tasks++;
		}

		public boolean finish(){
			tasks--;
			if(tasks == 0){
				getContext().stop(ref);
				log.info("Stopping "+this.toString());
				return true;
			}
			return false;
		}

		public String toString(){
			return ref.path().name();
		}
	}

	ArrayList<WorkerWrapper> workers;
	int lastWorker;
	int maxWorkers;
	int lastId;

	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public LoadBalancer(int maxWorkers) {
		workers = new ArrayList<>();
		lastWorker = 0;
		this.maxWorkers = maxWorkers;
		lastId = 0;
	}

	// Static function creating actor
	public static Props createActor(int maxWorkers) {
		return Props.create(LoadBalancer.class, () -> {
			return new LoadBalancer(maxWorkers);
		});
	}

	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof Task){
			if(maxWorkers > workers.size()){
				WorkerWrapper w = new WorkerWrapper(lastId++);
				w.doTask((Task) message);
				workers.add(w);
			}else{
				workers.get(lastWorker).doTask((Task) message);
				lastWorker = (lastWorker + 1) % workers.size();
			}
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] to delegate task");

		}

		if(message instanceof TaskFinished){
			for (WorkerWrapper w:workers
				 ) {
				if(w.toString().equalsIgnoreCase(getSender().path().name())){
					if(w.finish()){
						workers.remove(w);
						break;
					}
				}
			}
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with finished task");
		}

	}

}
