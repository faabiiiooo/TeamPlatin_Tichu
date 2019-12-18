package resources;

//@author Fabio
public class MessageResponse extends Message {

    private final String parentMessageID;

    public MessageResponse(String type, String message, String parentMessageID){
        super("response/"+type, message);
        this.parentMessageID = parentMessageID;
    }

    public String getParentMessageID(){ return parentMessageID; }

}
