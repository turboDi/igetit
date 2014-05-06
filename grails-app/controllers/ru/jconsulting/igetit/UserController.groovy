package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import ru.jconsulting.igetit.auth.User

@Secured(['ROLE_USER'])
class UserController extends RestfulController<User> {

    static responseFormats = ['json']

    UserController() {
        super(User)
    }
}
