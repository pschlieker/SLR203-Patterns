
package demo;

import akka.actor.ActorRef;

public class MyMessage {
    public final String data;
    public final ActorRef actorRef;

    public MyMessage(String data, ActorRef actorRef) {
        this.data = data;
        this.actorRef = actorRef;
    }

    public ActorRef getReceiver(){
        return actorRef;
    }
}