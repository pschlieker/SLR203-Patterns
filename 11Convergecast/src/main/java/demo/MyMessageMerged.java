
package demo;

import akka.actor.ActorRef;

public class MyMessageMerged {
    public final String data;
    public String senders;

    public MyMessageMerged(String data,String senders) {
        this.data = data;
        this.senders = senders;
    }
}