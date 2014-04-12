package ru.jconsulting.igetit

import org.grails.rateable.Rateable

class Comment implements Rateable {

    Date created

    String text

    Person author

    static belongsTo = [buy : Buy]

    static constraints = {
        text maxSize: 1000
    }
}
