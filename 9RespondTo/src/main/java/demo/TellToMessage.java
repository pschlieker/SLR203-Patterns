
package demo;

import akka.actor.ActorRef;

public class TellToMessage {
    public final String data;
    public final ActorRef actorRef;

    public TellToMessage(String data, ActorRef actorRef) {
        this.data = data;
        this.actorRef = actorRef;
    }

    public ActorRef getReceiver(){
        return actorRef;
    }
}