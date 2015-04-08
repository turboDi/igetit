package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

@Secured(['ROLE_USER'])
class PersonController extends RestfulController<Person> {

    PersonController() {
        super(Person)
    }
}
