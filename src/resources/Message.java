package resources;

import java.io.Serializable;
import java.util.ArrayList;

public class Message implements Serializable {

    private static final long serialVersionUID= 1;

    private static long messageID = 0;
    private long id;

    private final ArrayList<Object> objects = new ArrayList<>();
    private String type;

    public Message(String type, Object ... obj){
        this.id = messageID++;
        this.type = type;
        for(Object o : obj) {
            objects.add(o);
        }
    }

    public ArrayList<?> getObjects() {
        return objects;
    }

    public String getType() {
        return type;
    }
}
