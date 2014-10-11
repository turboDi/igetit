package ru.jconsulting.igetit

import ru.jconsulting.likeable.Likeable

class Comment implements Likeable {

    Date dateCreated

    String text

    Person author

    static belongsTo = [buy : Buy]

    static constraints = {
        text maxSize: 1000
    }
}
