package resources;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

//@author Fabio
public class Message implements Serializable {

    protected static final long serialVersionUID= 1;

    protected final String messageID;
    protected int senderID;

    protected final ArrayList<Object> objects = new ArrayList<>();
    protected String type;

    protected MessageStats messageStatus;

    public Message(String type, Object ... obj){
        messageID = UUID.randomUUID().toString();
        this.type = type;
        for(Object o : obj) {
            objects.add(o);
        }
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public String getType() {
        return type;
    }

    public String getMessageID(){ return messageID; }

    public void setMessageStatus(MessageStats messageStatus){ this.messageStatus = messageStatus;}

    public MessageStats getMessageStatus(){ return messageStatus; }

    public void setSenderID(int senderID) { this.senderID = senderID; }

    public int getSenderID() {
        return senderID;
    }
}
