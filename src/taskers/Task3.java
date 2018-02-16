/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;

/**
 *
 * @author dalemusser
 * 
 * This example uses PropertyChangeSupport to implement
 * property change listeners.
 * 
 */
public class Task3 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    enum taskStatus {STARTED, INPROGRESS, STOPPED, FINISHED};
    private taskStatus status;
    
    private String message;
    
    public Task3(int maxValue, int notifyEvery)  {
        status = taskStatus.STOPPED;
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
    }
    
    @Override
    public void run() {
        doNotify(status.STARTED,0);
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify(status.INPROGRESS, i);
            }
            
            if (exit) {
                doNotify(status.STOPPED, 0);
                return;
            }
        }
        doNotify(status.FINISHED, 0);
    }
    
    public void end() {
        exit = true;
    }
    
    // the following two methods allow property change listeners to be added
    // and removed
    public void addPropertyChangeListener(PropertyChangeListener listener) {
         pcs.addPropertyChangeListener(listener);
     }

     public void removePropertyChangeListener(PropertyChangeListener listener) {
         pcs.removePropertyChangeListener(listener);
     }
    
    private void doNotify(taskStatus status, int count) {
        // this provides the notification through the property change listener
        Platform.runLater(() -> {
            // I'm choosing not to send the old value (second param).  Sending "" instead.
            if(null == status){
                    message = "AN ERROR OCCURED";
                } else switch(status){
                    case STARTED:
                        message = "Started Task3";
                        break;
                    case INPROGRESS:
                        message = "Happened in Task3: " + count;
                        break;
                    case FINISHED:
                        message = "Task3 done.";
                        break;
                    case STOPPED:
                        message = "Task3 stopped";
                        break;
                    default:
                        message = "ERROR";
                        break;
                }
            pcs.firePropertyChange("message", "", message);
        });
    }
}