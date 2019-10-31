package server.model;

import resources.ServiceLocator;

import java.net.Socket;
import java.util.logging.Logger;

public class Srv_ClientThread extends Thread {

    private static int clientId = 0;

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();

    public Srv_ClientThread(Socket socket) {
        super("ClientThread for Client " + clientId);
        logger.info("Created ClientThread for Client: " + clientId);
        clientId++;
    }

    public void run(){

    }

}
