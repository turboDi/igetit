package ru.jconsulting.igetit

import ru.jconsulting.likeable.Likeable

class Comment implements Likeable {

    Date dateCreated

    String text

    Person author

    boolean deleted

    static belongsTo = [buy : Buy]

    static constraints = {
        text maxSize: 1000
        deleted bindable: false
    }
}
