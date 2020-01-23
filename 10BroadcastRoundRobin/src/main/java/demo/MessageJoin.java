package demo;

import akka.actor.ActorRef;

public class MessageJoin{
    ActorRef join;
    public MessageJoin(ActorRef join){
        this.join = join;
    }
    public ActorRef getMember(){
        return join;
    }
}