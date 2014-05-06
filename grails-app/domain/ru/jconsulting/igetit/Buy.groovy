package ru.jconsulting.igetit

import org.grails.rateable.Rateable
import ru.jconsulting.igetit.auth.User

class Buy implements Rateable {

    String name

    Brand brand

    Category category

    User owner

    Price price

    Image image

    Date created

    String description

    static hasMany = [comments : Comment]

    static constraints = {
        brand nullable: true
        category nullable: true
        description nullable: true
    }
}
