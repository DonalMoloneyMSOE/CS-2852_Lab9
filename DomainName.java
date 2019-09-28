/*
 *CS 2852 - 011
 *Fall 2017
 *Lab 9 - DNS Server
 *Name: Donal Moloney
 *Created: 11/1/2017
 */
package Moloneyda;

/**
 *
 */
public class DomainName {
    String domainName;

    /**
     * This is the default domain name constructor
     */
    public DomainName() {}

    /**
     * This is the overriden domain name cosntrucror it vlaidate a passed in domain name and sets
     * the domain name
     * @param name - a domain name to validate
     * @throws IllegalArgumentException - if a domain name is not valid
     */
    public DomainName(String name) throws IllegalArgumentException {
        validateDomainName(name);
        domainName = name;
    }


    /**
     * This method validates the domain name of when creating a new domain name object
     * @param name - a domain name to validate
     */
    private void validateDomainName(String name) throws IllegalArgumentException {
        if (name.length() < 253) {
            if (Character.isDigit(name.charAt(0)) == false &&
                    Character.isLetter(name.charAt(0)) == false) {
                throw new IllegalArgumentException(
                        "contains illegal character at front of " + "domain\t" + name);
            } else if (name.substring(0, 1).equals(".") || name.substring(0, 1).equals("-")) {
                throw new IllegalArgumentException("The domain name is in the incorrect format");
            } else if (name.contains("..") || name.contains("--")) {
                throw new IllegalArgumentException("The domain name is in the incorrect format");
            }
        } else {
            throw new IllegalArgumentException("The domain name is in the incorrect format");
        }
    }

    /**
     * @return the domain name of the object as a string
     */
    public String toString() {
        return domainName;
    }


    /**
     * This is the overriden DomainName equals method it compares if two domain names are equal
     * to one another
     * @param object - a domain name to comapre to
     * @return - boolean value whether the two domain names are equal or not
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object == null) {
            return false;
        } else if (object instanceof DomainName) {
            DomainName castName = (DomainName) object;
            if (castName.toString().equalsIgnoreCase(this.toString())) {
                return true;
            }
        }
        return false;
    }
}
