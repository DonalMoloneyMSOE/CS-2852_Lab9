/*
 *CS 2852 - 011
 *Fall 2017
 *Lab 9 - DNS Server
 *Name: Donal Moloney
 *Created: 11/1/2017
 */
package Moloneyda;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField domainNameField;
    @FXML
    private TextField ipAddressField;
    private boolean started = false;
    private static final String DNS_ENTERIES =
            "C:/Users/moloneyda/CS2852Labs/src/Moloneyda/dnsentries.txt";
    private static final String DNS_UPDATE =
            "C:/Users/moloneyda/CS2852Labs/src/Moloneyda/update.txt";
    private DNS dns;
    private Alert alertError;
    private Alert alertExit;
    private Alert alertInfo;
    private boolean contains;


    /**
     * This is the default constructor for the controller class it intilizes the alert boxes and
     * initializes the dns object
     */
    public Controller() {

        alertInfo = new Alert(Alert.AlertType.INFORMATION);
        alertExit = new Alert(Alert.AlertType.CONFIRMATION);
        alertError = new Alert(Alert.AlertType.ERROR);
        dns = new DNS(DNS_ENTERIES);
    }


    /**
     * This program starts the dns server
     *
     * @param actionEvent - a button click
     */
    @FXML
    public void start(ActionEvent actionEvent) {
        started = true;
        startButton.setDisable(true);
        stopButton.setDisable(false);
        updateButton.setDisable(false);
        addButton.setDisable(false);
        deleteButton.setDisable(false);
        ipAddressField.setDisable(false);
        domainNameField.setDisable(false);
        try {
            dns.setStarted(started);
            dns.start();
        } catch (IOException e) {
            String error = e.getMessage();
            alertError.setTitle("Error Dialog");
            alertError.setHeaderText("An error has occurred");
            alertError.setContentText("The error occurred because: " + error);
            alertError.showAndWait();
        }
    }

    /**
     * This button stops the gui by disabling its feature buttons, but does not exit the program
     * it also writes the contents of the gui to a file
     *
     * @param actionEvent - a button click
     */
    @FXML
    public void stop(ActionEvent actionEvent) {
        updateButton.setDisable(true);
        addButton.setDisable(true);
        deleteButton.setDisable(true);
        stopButton.setDisable(true);
        startButton.setDisable(false);
        started = false;
        dns.setStarted(started);
        boolean stop = dns.stop();
        if (stop == true) {
            alertInfo.setTitle("Information Dialog");
            alertInfo.setHeaderText("Information Dialog");
            alertInfo.setContentText("Your program successfully stopped!");

            alertInfo.showAndWait();
        } else {
            alertError.setTitle("Error Dialog");
            alertError.setHeaderText("An error has occurred");
            alertError.setContentText("The program did not stop successfully!");
            alertError.showAndWait();
        }
        dns.clear();
    }

    /**
     * This method exits the program
     *
     * @param actionEvent - a button click
     */
    @FXML
    public void exit(ActionEvent actionEvent) {
        alertExit.setTitle("Confirmation Dialog");
        alertExit.setHeaderText("Look, a Confirmation Dialog");
        alertExit.setContentText("The program is exiting?");

        Optional<ButtonType> result = alertExit.showAndWait();
        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * This method gets the text in a text field and checks to see if the domain name is
     * contained in the server and tells the user if it is or not
     *
     * @param keyEvent - a key press
     */
    @FXML
    public void getTextDomainName(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (started == true) {
                String userInput = domainNameField.getText();
                if (!userInput.isEmpty()) {
                    DomainName domainName = new DomainName(userInput);
                    String lookUpResponse = dns.lookup(domainName, started);
                    if (lookUpResponse != null) {
                        ipAddressField.setText(lookUpResponse);
                    } else {
                        domainNameField.clear();
                        domainNameField.setText("Not Found");
                    }
                }
            }
        }
    }

    /**
     * This method handles the
     *
     * @param actionEvent - a button press
     */
    @FXML
    public void update(ActionEvent actionEvent) {
        try {
            dns.parseUpdate(started, DNS_UPDATE);
        } catch (IOException e) {
            String error = e.getMessage();
            alertError.setTitle("Error Dialog");
            alertError.setHeaderText("An error has occurred");
            alertError.setContentText("The error occurred because: " + error);
            alertError.showAndWait();
        }
    }

    /**
     * This method deletes and ip address from the collection
     *
     * @param actionEvent - a button click
     */
    public void deleteIp(ActionEvent actionEvent) {
        String ipToDelete = ipAddressField.getText();
        if (ipToDelete.isEmpty()) {
            try {
                IPAddress ipDelete = new IPAddress(ipToDelete);
                String domainToDelete = domainNameField.getText();
                DomainName domainDelete = new DomainName(domainToDelete);
                dns.deleteEntry(domainDelete, ipDelete);
            } catch (IllegalArgumentException e) {
                String error = e.getMessage();
                alertError.setTitle("Error Dialog");
                alertError.setHeaderText("An error has occurred");
                alertError.setContentText("The error occurred because: " + error);
                alertError.showAndWait();
            }
        }
    }

    /**
     * This method adds an ipaddress to the colelction
     *
     * @param actionEvent - a button click
     */
    public void addIp(ActionEvent actionEvent) {
        String ipToAdd = ipAddressField.getText();
        if (!ipToAdd.isEmpty()) {
            try {
                IPAddress ipAdd = new IPAddress(ipToAdd);
                String domainToAdd = domainNameField.getText();
                DomainName domainAdd = new DomainName(domainToAdd);
                dns.addToMap(domainAdd, ipAdd);
            }catch (IllegalArgumentException e){
                String error = e.getMessage();
                alertError.setTitle("Error Dialog");
                alertError.setHeaderText("An error has occurred");
                alertError.setContentText("The error occurred because: " + error);
                alertError.showAndWait();
            }
        }
    }
}
