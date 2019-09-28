/*
 *CS 2852 - 011
 *Fall 2017
 *Lab 9 - DNS Server
 *Name: Donal Moloney
 *Created: 11/1/2017
 */
package Moloneyda;

import java.util.InputMismatchException;

public class IPAddress {
    private String ipAddress;

    /**
     * This is the default constructor for the ipAddress class
     */
    public IPAddress(){}

    /**
     * This is one of the overloaded constructors for the ipAddress class it validates an
     * ip address and sets its value
     * @param newAddress
     * @throws IllegalArgumentException
     */
    public IPAddress(String newAddress) throws IllegalArgumentException {
        validateIp(newAddress);
        ipAddress = newAddress;
    }

    /**
     * This method runs through tests to validate the ip
     * @param newAddress an ip address to validate
     * @throws InputMismatchException - if the ip address does not follow the ipv4 specification
     */
    private void validateIp(String newAddress) throws InputMismatchException {
        String[] ipv4Parts = newAddress.split("\\.");
        if (ipv4Parts.length == 4) {
            for (int i = 0; i < ipv4Parts.length; i++) {
                int integer = testOnlyIntegers(ipv4Parts[i]);
                testCorrectRange(integer);
            }
        } else {
            throw new IllegalArgumentException("Your IP address did not follow IpV4 format");
        }
    }

    /**
     * This method checks to see if a part of the ip address is in the specified range
     * @param integer -
     * @throws InputMismatchException - if on of the 4 ip pats is out of range
     */
    private void testCorrectRange(int integer) throws InputMismatchException {
        if (integer <= 0 && integer >= 250) {
            throw new InputMismatchException("The ip address you entered contains values that are" +
                                                     " not in range the range should be between 0 " +
                                                     "and 250");
        }
    }

    /**
     * This method tests if the ip address is only integers
     * @param ipv4Part - a part of the ip address
     * @return the number of parts of the ip address
     * @throws InputMismatchException - if the ip address contains non-integer characters
     */
    private int testOnlyIntegers(String ipv4Part) throws InputMismatchException {
        int integer = Integer.parseInt(ipv4Part);
        for (int i = 0; i < ipv4Part.length(); i++) {
            if (!Character.isDigit(ipv4Part.charAt(i))) {
                throw new IllegalArgumentException(
                        "The ip address you entered contains non-number characters");

            }
        }
        return integer;
    }

    /**
     * @return the ip address as a string
     */
    public String toString() {
        return ipAddress;
    }

    /**
     * This is the overridden equals method it compares if two ip address are the same
     * @param address - the address that this objects address is comparing itself too.
     * @return boolean value if the ip address is the same or not
     */
    @Override
    public boolean equals(Object address) {
        if (this == address) {
            return true;
        } else if (address == null) {
            return false;
        } else if (address instanceof IPAddress) {
            IPAddress castAddress = (IPAddress) address;
            if (this.toString().equalsIgnoreCase(castAddress.toString())) {
                return true;
            }
        }
        return false;
    }
}
