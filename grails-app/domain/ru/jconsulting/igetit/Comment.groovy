package ru.jconsulting.igetit

import org.grails.rateable.Rateable
import ru.jconsulting.igetit.auth.User

class Comment implements Rateable {

    Date created

    String text

    User author

    static belongsTo = [buy : Buy]

    static constraints = {
        text maxSize: 1000
    }
}
