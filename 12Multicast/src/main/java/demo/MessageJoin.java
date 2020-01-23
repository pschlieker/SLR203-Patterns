package demo;

import akka.actor.ActorRef;

public class MessageJoin{
    ActorRef[] join;
    String group;

    public MessageJoin(String group, ActorRef[] join){
        this.join = join;
        this.group = group;
    }
    public ActorRef[] getMember(){
        return join;
    }

    public String getGroup(){
        return group;
    }
}