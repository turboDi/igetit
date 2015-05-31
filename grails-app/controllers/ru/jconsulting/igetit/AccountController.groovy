package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*

@Secured(['permitAll'])
class AccountController {

    static allowedMethods = [register: "POST", verify: "GET"]

    def accountService
    def emailService
    def grailsApplication
    def restAuthenticationTokenJsonRenderer

    def register(String oAuthProvider) {
        def token = null
        params << request.JSON
        params.oAuthProvider = oAuthProvider
        if (!oAuthProvider) {
            params.email = params.username
        } else {
            params.password = 'N/A'
            token = accountService.tryReAuthenticate(params.username as String, oAuthProvider)
        }
        if (!token) {
            def p = new Person(params)
            p.validate()
            if (p.hasErrors()) {
                response.status = UNPROCESSABLE_ENTITY.value()
                render p.errors as JSON
                return
            }
            token = accountService.register(p)
        }
        render JSON.parse(restAuthenticationTokenJsonRenderer.generateJson(token)) as JSON
    }

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
