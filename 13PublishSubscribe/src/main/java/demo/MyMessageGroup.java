
package demo;

import akka.actor.ActorRef;

public class MyMessageGroup {
    public final String data;
    public String receivers;

    public MyMessageGroup(String data, String receivers) {
        this.data = data;
        this.receivers = receivers;
    }
}