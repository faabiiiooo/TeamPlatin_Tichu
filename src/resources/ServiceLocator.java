package resources;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;


import client.Tichu_Clt;
import client.controller.Clt_Controller;
import client.model.Clt_Client;
import server.model.Srv_Model;
import server.model.Srv_Server;
import server.model.Srv_Table;

/**
 * Copyright 2015, FHNW, Prof. Dr. Brad Richards. All rights reserved. This code
 * is licensed under the terms of the BSD 3-clause license (see the file
 * license.txt).
 * 
 * The singleton instance of this class provide central storage for resources
 * used by the program. It also defines application-global constants, such as
 * the application name.
 * 
 * @author Brad Richards
 */
public class ServiceLocator {
    private static ServiceLocator serviceLocator; // singleton

    // Application-global constants
    final private Class<?> APP_CLASS = Tichu_Clt.class;
    final private String APP_NAME = "Tichu";
    
    // Supported locales (for translations)
    final private Locale[] locales = new Locale[] { new Locale("en"), new Locale("de") };

    // Resources
    private Logger logger;
    //private Configuration configuration;
    private Translator translator;
    private Clt_Client client;
    private Srv_Server server;
    private Srv_Table table;
    private Srv_Model srvModel;
    private Clt_Controller cltController;

    /**
     * Factory method for returning the singleton
     */
    public static ServiceLocator getServiceLocator() {
        if (serviceLocator == null) {
            serviceLocator = new ServiceLocator();
            serviceLocator.setLogger(configureLogging());
        }
        return serviceLocator;
    }

    private static Logger configureLogging(){

        Logger rootLogger = Logger.getLogger("");
        Logger ourLogger = Logger.getLogger(serviceLocator.getAPP_NAME());
        ourLogger.setLevel(Level.INFO);

        return ourLogger;

    }


    /**
     * Private constructor, because this class is a singleton
     */
    private ServiceLocator() {
        // Currently nothing to do here. We must define this constructor anyway,
        // because the default constructor is public
    }

    public Class<?> getAPP_CLASS() {
        return APP_CLASS;
    }
    
    public String getAPP_NAME() {
        return APP_NAME;
    }

    public Logger getLogger() {
        return logger;
    }

    public Clt_Controller getCltController() { return cltController; }

    public Srv_Model getSrvModel() { return srvModel; }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public void setCltController(Clt_Controller cltController){ this.cltController = cltController;}

    public void setSrvModel(Srv_Model srvModel) { this.srvModel = srvModel; }

    /*public Configuration getConfiguration() {
        return configuration;
    }*/

    /*public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }*/

    public Clt_Client getClient() {
        return client;
    }

    public void setClient(Clt_Client client) {
        this.client = client;
    }

    public Srv_Server getServer() {
        return server;
    }

    public void setServer(Srv_Server server) {
        this.server = server;
    }

    public Locale[] getLocales() {
        return locales;
    }

    public Translator getTranslator() {
        return translator;
    }
    
    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public Srv_Table getTable() {
        return table;
    }

    public void setTable(Srv_Table table) {
        this.table = table;
    }
}
