/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import java.util.ArrayList;
import javafx.application.Platform;

/**
 *
 * @author dalemusser
 * 
 * This example uses a Notification functional interface.
 * This allows the use of anonymous inner classes or
 * lambda expressions to define the method that gets called
 * when a notification is to be made.
 */
public class Task2 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private ArrayList<Notification> notifications = new ArrayList<>();
    
    enum taskStatus {STARTED, INPROGRESS, STOPPED, FINISHED};
    private taskStatus status; 
    
    private String message;
    
    public Task2(int maxValue, int notifyEvery)  {
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
    
    // this method allows a notification handler to be registered to receive notifications
    public void setOnNotification(Notification notification) {
        this.notifications.add(notification);
    }
    
    private void doNotify(taskStatus status, int count) {
        // this provides the notification through the registered notification handler
        for (Notification notification : notifications) {
            Platform.runLater(() -> {
                if(null == status){
                    message = "AN ERROR OCCURED";
                } else switch(status){
                    case STARTED:
                        message = "Started Task2";
                        break;
                    case INPROGRESS:
                        message = "Happened in Task2: " + count;
                        break;
                    case FINISHED:
                        message = "Task2 done.";
                        break;
                    case STOPPED:
                        message = "Task2 stopped";
                        break;
                    default:
                        message = "ERROR";
                        break;
                }
                notification.handle(message);
            });
        }
    }
}