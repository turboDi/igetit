package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

class AccountController {

    static namespace = "v1"

    def emailService
    def accountService

    @Secured(['permitAll'])
    def verify(String key) {
        Person p
        if (key && (p = emailService.verify(key))) {
            redirect(url: grailsApplication.config.site.url + '?name=' + URLEncoder.encode(p.fullName, 'UTF-8'))
        } else {
            render status: NOT_FOUND
        }
    }

    @Secured(['permitAll'])
    def resetPassword(String email) {
        Person p
        if (!email || !(p = Person.findByEmail(email))) {
            render status: NOT_FOUND
        } else {
            emailService.sendPassword(p, accountService.resetPassword(p))
            render status: OK
        }
    }

    @Secured(['ROLE_USER'])
    def sendVerification() {
        Person p = getAuthenticatedUser() as Person
        emailService.sendVerification(p)
        render status: OK
    }
}
