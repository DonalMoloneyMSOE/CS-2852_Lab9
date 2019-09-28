/*
 *CS 2852 - 011
 *Fall 2017
 *Lab 9 - DNS Server
 *Name: Donal Moloney
 *Created: 11/1/2017
 */
package Moloneyda;

import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

public class DNS {
    private String filename;
    Alert alert;
    boolean contains;

    Map<DomainName, IPAddress> map;
    private boolean started;

    /**
     * This is the default constructor for the DNS class
     */
    public DNS(){}

    /**
     * This is the overridden constructor it initializes object and sets the filename
     * @param filename - the file to read
=     */
    public DNS(String filename) {
        alert = new Alert(Alert.AlertType.ERROR);
        map = new HashMap<DomainName, IPAddress>();
        this.filename = filename;
    }


    /**
     * This method parses the file and puts the informaton into a map
     * @throws IOException - when an error occurs reading the file
     */
    public boolean start() throws IOException {
        IPAddress ipAddress = null;
        DomainName domainName = null;
        String fileToRead = filename;
        BufferedReader bufferedReader;
        FileReader fileReader;
        String currentLine;
        String[] characters;
        fileReader = new FileReader(fileToRead);
        bufferedReader = new BufferedReader(fileReader);
        while ((currentLine = bufferedReader.readLine()) != null) {
            try {
                characters = currentLine.split("\\s+");
                ipAddress = new IPAddress(characters[0]);
                domainName = new DomainName(characters[1]);
                map.put(domainName, ipAddress);
            } catch (IllegalArgumentException e) {
                String message = e.getMessage();
                String[] exceptionMessage = message.split("\t");
                System.err.println(
                        "Skipping\t" + exceptionMessage[1] + " because " + exceptionMessage[0]);
            }

        }
        return true;
    }

    /**
     * This method lookups to see if a domain name is contained in the collection only after the
     * @param domainName - the domain name to search for
     * @param started - if the program has started or not
     * @return the ip address of the domain name searchin for
     */
    public String lookup(DomainName domainName, boolean started) {
        if (started == true) {
            for (Map.Entry<DomainName, IPAddress> entry : map.entrySet()) {
                if (entry.getKey().toString().equals(domainName.toString())) {
                    return entry.getValue().toString();
                }
            }
            return null;
        }
        return null;
    }

    /**
     * This method parses the update file and passes it to the update function
     * @param started - if the program has been started
     * @param fileName - the name of the file to write to
     * @throws IOException - if an error occurs reading from the file
     * @throws IllegalArgumentException - if an illegal argument is encountered writing to the file
     */
    public String parseUpdate(boolean started, String fileName) throws IOException,
                                                                       IllegalArgumentException {
        if (started == true) {
            BufferedReader bufferedReader;
            FileReader fileReader;
            String currentLine;
            fileReader = new FileReader(fileName);
            bufferedReader = new BufferedReader(fileReader);
            while ((currentLine = bufferedReader.readLine()) != null) {
                update(currentLine);
            }
        }
        return null;
    }

    /**
     * This method validates the parts of the update file before trying to update the txt file
     * @param line- a line of the update file
     * @return null if started has not occured, or returns A or D if it has added or delted a
     * value this is purely symbolic
     * @throws IllegalArgumentException
     */
    private String update(String line) throws IllegalArgumentException {
        if (started == true) {
            String[] parts = line.split("\\s+");
            String command = parts[0];
            checkCommand(command);
            IPAddress updateIp = new IPAddress(parts[1]);
            DomainName updateDomain = new DomainName(parts[2]);
            if (command.equals("ADD")) {
                addToMap(updateDomain, updateIp);
                return "A";
            } else {
                deleteEntry(updateDomain, updateIp);
                return "D";
            }
        } else {
            return null;
        }

    }

    /**
     * This method checks to see if the update file has the correct commands
     * @param command the command to check if its correct
     * @throws IllegalArgumentException - if an incorrect command is encountered
     */
    private void checkCommand(String command) throws IllegalArgumentException {
        if (!command.equalsIgnoreCase("ADD") && !command.equalsIgnoreCase("DEL")) {
            throw new IllegalArgumentException("Your command was incorrect");
        }
    }

    /**
     *This method clears the contents of the map collection
     */
    public void clear() {
        map = new HashMap<>();
    }

    /**
     * This method is an overridden lookup method it checks to see if a matching domain name has
     * a matching ip address
     *
     * @param domainName - the domain name to match
     * @param ipAddress - the ip address to match
     * @return
     */
    public boolean lookup(DomainName domainName, IPAddress ipAddress) {
        for (Map.Entry<DomainName, IPAddress> entry : map.entrySet()) {
            if (entry.getKey().toString().equals(domainName.toString())) {
                if (entry.getValue().toString().equals(ipAddress)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method deletes a domain name from the collection
     *
     * @param domainDelete - the domain name to delete
     * @param ipDelete     - the ip address to delete
     * @return - the ip address of the domain name removed
     */
    public IPAddress deleteEntry(DomainName domainDelete, IPAddress ipDelete) throws
                                                                              InputMismatchException {
        String contains = lookup(domainDelete, started);
        if (contains != null) {
            if (map.get(domainDelete).toString().equals(ipDelete)) {
                IPAddress temp = map.get(domainDelete);
                map.remove(domainDelete, ipDelete);
                return temp;
            } else {
                throw new InputMismatchException(
                        "The domain name exist but the Ip address does " + "not match");
            }
        }
        return null;
    }

    /**
     * This method adds a new entry to our collection of domain names
     *
     * @param domainAdd - the domain name being added to the map
     * @param ipAdd     - the ip being added to the map
     * @return null if there is no prexiting enrty with the same domain name or the ip
     * address of the domain name entry you are overwriting
     */
    public IPAddress addToMap(DomainName domainAdd, IPAddress ipAdd) {
        String contains = lookup(domainAdd, started);
        if (contains != null) {
            map.remove(domainAdd);
            map.put(domainAdd, ipAdd);
            return null;
        } else {
            IPAddress temp = map.get(domainAdd);
            map.put(domainAdd, ipAdd);
            return temp;
        }
    }

    /**
     * This method writes an ip address to the file
     */
    public void write() {
        try {
            FileWriter writer = new FileWriter(filename);
            for (Map.Entry<DomainName, IPAddress> entry : map.entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.print("An error occurred writing to the file");
        }

    }

    /**
     * This method stops the program
     * @return - returns ture
     */
    public boolean stop() {
        write();
        return true;
    }

    /**
     * this method sets the start
     * @param started - whethere the program has started or not
     */
    public void setStarted(boolean started) {
        this.started = started;
    }
}
