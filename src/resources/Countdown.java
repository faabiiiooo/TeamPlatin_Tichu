package resources;

import javafx.beans.property.SimpleIntegerProperty;
import resources.ServiceLocator;
import java.util.logging.Logger;

public class Countdown extends Thread { //@author Sandro Countdown is a Thread

    private static ServiceLocator sl = ServiceLocator.getServiceLocator();
    private static Logger logger = sl.getLogger();
    private int current = 0; //current value of the countdown
    private int minimum = 0; //minimum value of the countdown
    private int maximum = 30; //30 second time for a move
    private boolean pause = true;
    private SimpleIntegerProperty currentProperty = new SimpleIntegerProperty();

    public Countdown() {
        super("CountdownThread");
        this.setDaemon(true); //automatically close Thread if program close
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(1); //1 count = 1 second
            } catch (Exception e) {
                logger.info("Exception: "+e);
            }

            if (pause == false) { //countdown not paused? start to count
                if (current <= maximum) {
                    current++;
                    this.currentProperty.set(current);
                    logger.info("Counter: "+current);
                }

                if (current+1 > maximum) { //if maximum reached -> stopCountdown
                    stopCountdown();
                    logger.info("Countdown finished");
                    break;
                }
            }
        }
    }

    public void startCountdown() {
        setCountdownPause(false);
        super.start(); //start thread
        logger.info("Start Countdown");
    }

    public void stopCountdown() {
        setCountdownPause(true);
        resetCountdown();
        logger.info("Stop Countdown");
    }

    public void resetCountdown() {
        current = minimum;
        this.currentProperty.set(current);
        logger.info("Reset Countdown");
    }

    public boolean CountdownIsPause() {
        return this.pause;
    }

    public void setCountdownPause(boolean pause) {
        this.pause = pause;
        if (this.pause == true) {
            logger.info("Pause Countdown");
        } else {
            logger.info("UnPause Countdown");
        }
    }

    public double getCurrent() { // Double Value to change ProgressIndicator
        return (this.currentProperty.get()*0.0333333); // (1 divide with 30 seconds) * current
    }


    public SimpleIntegerProperty currentCountdownProperty() {
        return currentProperty;
    }
}
