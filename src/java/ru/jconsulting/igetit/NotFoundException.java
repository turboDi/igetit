package ru.jconsulting.igetit;

/**
 * @author Dmitriy Borisov
 * @created 9/27/2015
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String msg) {
        super(msg);
    }

}
