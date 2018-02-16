/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifcationexamples;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import taskers.*;

/**
 * FXML Controller class
 *
 * @author dalemusser
 */
public class NotificationsUIController implements Initializable, Notifiable {

    @FXML
    private TextArea textArea;
    @FXML
    private Button buttonTask1;
    @FXML
    private Button buttonTask2;
    @FXML
    private Button buttonTask3; 
    
    private Task1 task1;
    private Task2 task2;
    private Task3 task3;
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void start(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (task1 != null) task1.end();
                if (task2 != null) task2.end();
                if (task3 != null) task3.end();
            }
        });
    }
    
    @FXML
    public void startTask1(ActionEvent event) {
        System.out.println("start task 1");
        if (task1 == null) {
            task1 = new Task1(2147483647, 1000000);
            task1.setNotificationTarget(this);
            task1.start();
            buttonTask1.setText("Stop Task 1");
        }else{
            System.out.println("end task 1");
            task1.end();
            task1 = null;
            buttonTask1.setText("Start Task 1");
        }
    }
    
    @Override
    public void notify(String message) {
        if (message.equals("Task1 done.")) {
            task1 = null;
            buttonTask1.setText("Start Task 1");
        }
        textArea.appendText(message + "\n");
    }
    
    @FXML
    public void startTask2(ActionEvent event) {
        System.out.println("start task 2");
        if (task2 == null) {
            task2 = new Task2(2147483647, 1000000);
            
            task2.setOnNotification((String message) -> {
                if(message.equals("Task2 done.")){
                System.out.println("Task2 has ended");
                task2.end();
                task2 = null;
                buttonTask2.setText("Start Task 2");
                }
                textArea.appendText(message + "\n");
            });
            
            task2.start();
            
            buttonTask2.setText("Stop Task 1");
        }else{
            System.out.println("end task 2");
            task2.end();
            task2 = null;
            buttonTask2.setText("Start Task 2");
        }
    }
    
    @FXML
    public void startTask3(ActionEvent event) {
        System.out.println("start task 3");
        if (task3 == null) {
            task3 = new Task3(2147483647, 1000000);
            // this uses a property change listener to get messages
            task3.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                if(evt.getNewValue().equals("Task3 done.")){
                    System.out.println("task3 has ended");
                    task3.end();
                    task3 = null;
                    buttonTask3.setText("Start Task 3");
                }
                textArea.appendText((String)evt.getNewValue() + "\n");
            });
            task3.start();
            buttonTask3.setText("Stop Task 3");
        }else{
            System.out.println("end task 3");
            task3.end();
            task3 = null;
            buttonTask3.setText("Start Task 3");
        }
    } 
}
