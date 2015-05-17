package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class SubscriptionController {

    static allowedMethods = [tape: "GET", events: "GET"]

    def subscriptionService

    def tape() {
        render subscriptionService.tape(getAuthenticatedUser(), params) as JSON
    }

    def events() {
        render subscriptionService.events(getAuthenticatedUser(), params) as JSON
    }
}
