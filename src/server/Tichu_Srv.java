package server;

import server.controller.Srv_Controller;
import server.model.Srv_Countdown;
import server.model.Srv_Deck;
import server.model.Srv_Model;

public class Tichu_Srv {

    public static void main(String[] args){
        Srv_Model model = new Srv_Model();
        Srv_Controller controller = new Srv_Controller(model);

        Srv_Deck srv = new Srv_Deck(); //Testfall Deck
        System.out.println(srv.getCards().size());

        Srv_Countdown c1 = new Srv_Countdown(); //Testfall Countdown
        c1.startCountdown();
    }
}
