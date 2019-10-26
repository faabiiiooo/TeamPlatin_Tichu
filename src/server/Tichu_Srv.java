package server;

import server.controller.Srv_Controller;
import server.model.Srv_Model;

public class Tichu_Srv {

    public static void main(String[] args){
        Srv_Model model = new Srv_Model();
        Srv_Controller controller = new Srv_Controller(model);
    }
}
