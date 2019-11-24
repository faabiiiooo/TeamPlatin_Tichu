package server.model;

import resources.ServiceLocator;

import java.util.logging.Logger;

public class Srv_Model {

    private final ServiceLocator serviceLocator = ServiceLocator.getServiceLocator();
    private final Logger logger = serviceLocator.getLogger();

    private Srv_Game game;

    public Srv_Model(){

        Srv_Server server = new Srv_Server();
        server.start();
        createGame();

    }

    //@author Fabio
    private void createGame(){ //creating the game

        game = new Srv_Game();
        game.createTable();

    }

    private void startGame(){

        Srv_Round firstRound = new Srv_Round();
        game.getRounds().add(firstRound);
        logger.info("Created first Round");

        game.getTable().dealCards();
        logger.info("dealed cards");
    }
}
