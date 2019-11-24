package server.controller;

import resources.Message;
import server.model.Srv_Model;
import server.model.Srv_Player;

public class Srv_Controller { //Servercontroller is generated as a Singleton

    private Srv_Model model;
    private static Srv_Controller controller;

    //@author Fabio
    public static Srv_Controller getController(){
        if(controller == null){
            controller = new Srv_Controller();
        }
        return controller;
    }

    public void setModel(Srv_Model model){
        this.model = model;
    }


    //@author Fabio
    private Srv_Controller(){ //private constructor becaus Srv_Controller is a singleton

    }

    //@author Fabio
    public Message processIncomingMessage(Message msgIn) { // Generates Answermessage for every Incoming Message

        Message msgOut = null;

        switch (msgIn.getType()) {

            case "card":  //generating a Card from Message

                break;

            case "player":


                break;

            case "string":
                String incoming = (String) msgIn.getObjects().get(0);
                switch (incoming) {
                    case "skip":

                        break;

                    case "tichu"://Pascal
                        // TODO: 24.11.2019 ANtwort an den Client fehlt noch
                        for (Srv_Player p : model.getGame().getTable().getPlayersAtTable()) {//Each Player at Tabel
                            if (p.getPLAYER_ID() == msgIn.getSenderID()) {// Is the player ID equals to the Client ID
                                if (p.getHandCards().size() == 14) {//Check if Handcards equals 14
                                    p.setSaidSmallTichu(true);//Player is abel to call a small Tichu
                                } else {
                                    if (p.getHandCards().size() <= 8) {//Check if Handcards <8
                                        p.setSaidBigTichu(true);//Player is abel to call a bigTichu
                                    }
                                }
                            }
                            }


                        }

                        break;

                }

                return msgOut;
        }
    }
