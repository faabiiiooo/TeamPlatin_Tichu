package server.controller;

import resources.Message;
import server.model.Srv_Model;

public class Srv_Controller { //Servercontroller is generated as a Singleton

    private Srv_Model model;
    private static Srv_Controller controller;

    public static Srv_Controller getController(){ //Fabio
        if(controller == null){
            controller = new Srv_Controller();
        }
        return controller;
    }

    public void setModel(Srv_Model model){
        this.model = model;
    }

    private Srv_Controller(){ //private constructor becaus Srv_Controller is a singleton

    }

    public Message processIncomingMessage(Message msgIn){ // Generates Answermessage for every Incoming Message /Fabio

        Message msgOut = null;

        switch (msgIn.getType()){

            case "card":  //generating a Card from Message

            break;

            case "player":


            break;

            case "string":

            break;

        }

        return msgOut;
    }
}
