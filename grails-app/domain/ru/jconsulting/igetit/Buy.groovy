package ru.jconsulting.igetit

import ru.jconsulting.likeable.Likeable

class Buy implements Likeable {

    String name

    Category category

    Price price

    Shop shop

    City city

    Date dateCreated

    List images

    int likesCount

    boolean deleted

    static hasMany = [comments: Comment, images: Image]
    static belongsTo = [owner: Person]
    static embedded = ['price', 'city']

    static constraints = {
        name nullable: true
        category nullable: true
        price nullable: true
        shop nullable: true
        city nullable: true
        deleted bindable: false
    }

    static mapping = {
        images cascade: "all-delete-orphan"
        likesCount formula: "(select count(*) from l_like l where l.like_ref = id and l.type = 'buy')"
    }
}
