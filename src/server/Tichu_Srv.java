package server;

import server.controller.Srv_Controller;
import server.model.Srv_Model;

//@author Fabio
public class Tichu_Srv { //starts the server

    public static void main(String[] args){
        Srv_Model model = new Srv_Model();
        Srv_Controller controller = Srv_Controller.getController();
        controller.setModel(model);

    }
}
