package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*

class VerificationController {

    static namespace = "v1"
    static allowedMethods = [resend: "POST", verify: "GET"]

    def emailService

    @Secured(['permitAll'])
    def verify(String key) {
        Person p
        if (key && (p = emailService.verify(key))) {
            redirect(url: grailsApplication.config.site.url + '?name=' + URLEncoder.encode(p.fullName, 'UTF-8'))
        } else {
            render status: NOT_FOUND
        }
    }

    @Secured(['ROLE_USER'])
    def resend() {
        Person p = getAuthenticatedUser() as Person
        emailService.sendConfirmationEmail(p)
        render status: OK
    }
}
