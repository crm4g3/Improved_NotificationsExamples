/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import javafx.application.Platform;

/**
 *
 * @author dalemusser
 * 
 * This example uses an object passed in with a notify()
 * method that gets called when a notification is to occur.
 * To accomplish this the Notifiable interface is needed.
 * 
 */
public class Task1 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private Notifiable notificationTarget;
    
    private String message;
    
    enum taskStatus {STARTED, INPROGRESS, STOPPED, FINISHED};
    private taskStatus status; 
    
    public Task1(int maxValue, int notifyEvery)  {
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
    
    public void setNotificationTarget(Notifiable notificationTarget) {
        this.notificationTarget = notificationTarget;
    }
    
    private void doNotify(taskStatus status, int count) {
        // this provides the notification through a method on a passed in object (notificationTarget)
        if (notificationTarget != null) {
            Platform.runLater(() -> {
                if(null == status){
                    message = "AN ERROR OCCURED";
                } else switch(status){
                    case STARTED:
                        message = "Started Task1";
                        break;
                    case INPROGRESS:
                        message = "Happened in Task1: " + count;
                        break;
                    case FINISHED:
                        message = "Task1 done.";
                        break;
                    case STOPPED:
                        message = "Task1 stopped";
                        break;
                    default:
                        message = "ERROR";
                        break;
                }
                notificationTarget.notify(message);
            });
        }
    }
}
