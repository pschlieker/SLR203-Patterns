package demo;

import akka.actor.ActorRef;

public class MessageUnjoin {
    ActorRef join;
    public MessageUnjoin(ActorRef join){
        this.join = join;
    }
    public ActorRef getMember(){
        return join;
    }
}