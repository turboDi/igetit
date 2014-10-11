package ru.jconsulting.igetit

import ru.jconsulting.likeable.Likeable

class Buy implements Likeable {

    String name

    Brand brand

    Category category

    Person owner

    Price price

    Image image

    Date dateCreated

    static hasMany = [comments : Comment]

    static constraints = {
        name nullable: true
        brand nullable: true
        category nullable: true
        price nullable: true
        image nullable: true
    }
}
