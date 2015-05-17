package ru.jconsulting.igetit

import ru.jconsulting.likeable.Likeable

class Buy implements Likeable {

    String name

    Category category

    Price price

    Date dateCreated

    List images

    int likesCount

    static hasMany = [comments: Comment, images: Image]
    static belongsTo = [owner: Person]
    static embedded = ['price']

    static constraints = {
        name nullable: true
        category nullable: true
        price nullable: true
    }

    static mapping = {
        images cascade: "all-delete-orphan"
        likesCount formula: "(select count(*) from l_like l where l.like_ref = id and l.type = 'buy')"
    }
}
