package ru.jconsulting.igetit

import ru.jconsulting.likeable.Likeable

class Buy implements Likeable {

    String name

    Brand brand

    Category category

    Person owner

    Price price

    Date dateCreated

    List images

    static hasMany = [comments : Comment, images : Image]

    static constraints = {
        name nullable: true
        brand nullable: true
        category nullable: true
        price nullable: true
    }

    static mapping = {
        images cascade: "all-delete-orphan"
    }
}
