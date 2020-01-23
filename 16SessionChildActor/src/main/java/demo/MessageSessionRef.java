package demo;

import akka.actor.ActorRef;

public class MessageSessionRef {
    public ActorRef ref;
    public MessageSessionRef(ActorRef ref){
        this.ref = ref;
    }
}