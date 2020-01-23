
package demo;

import akka.actor.ActorRef;

public class Topology {
    boolean[][] adj;
    ActorRef[] refs;

    public Topology(boolean[][] adj, ActorRef[] refs){
        this.adj = adj;
        this.refs = refs;
    }
}