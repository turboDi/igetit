package ru.jconsulting.igetit

import groovy.transform.ToString

@ToString
class Event {

    Person effector

    Person initiator

    Buy buy

    Comment comment

    String type

    Date dateCreated

    static constraints = {
        buy nullable: true
        comment nullable: true
    }
}
