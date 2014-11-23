package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Secured(['ROLE_USER'])
class AccountController {

    def springSecurityService

    @Secured(['permitAll'])
    @Transactional
    def verify() {
        Person p = Person.findByConfirmTokenAndEmail(params.key, params.email)
        if (!p) {
            render status: NOT_FOUND
            return
        }
        p.emailConfirmed = true
        p.save()
        [person: p]
    }

    def profile() {
        params.id = springSecurityService.principal.id
        forward controller: 'person', action: 'show'
    }
}
