package ru.jconsulting.igetit

import org.grails.rateable.Rateable

class Buy implements Rateable {

    String name

    Brand brand

    Category category

    Person owner

    Price price

    Image image

    Date created

    String description

    static hasMany = [comments : Comment]

    static constraints = {
        brand nullable: true
        category nullable: true
        description nullable: true
        image nullable: true
    }
}
