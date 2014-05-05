package ru.jconsulting.igetit

import grails.rest.RestfulController

class PersonController extends RestfulController<Person> {

    static responseFormats = ['json']

    PersonController() {
        super(Person)
    }
}
