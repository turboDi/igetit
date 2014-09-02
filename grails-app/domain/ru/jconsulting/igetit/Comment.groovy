package ru.jconsulting.igetit

import ru.jconsulting.likeable.Likeable

class Comment implements Likeable {

    Date created

    String text

    Person author

    static belongsTo = [buy : Buy]

    static constraints = {
        text maxSize: 1000
    }
}
