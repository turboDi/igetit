package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.NOT_FOUND

@Secured(['ROLE_USER'])
class SubscriptionController {

    def subscriptionService

    def follow() {
        def person = Person.get(params.id)
        if (person == null) {
            render status: NOT_FOUND
            return
        }
        subscriptionService.follow(person, getAuthenticatedUser())
        render "${PersonFollower.countByPerson(person)}"
    }

    def tape() {
        render subscriptionService.tape(getAuthenticatedUser(), params) as JSON
    }

    def events() {
        render subscriptionService.events(getAuthenticatedUser(), params) as JSON
    }
}
