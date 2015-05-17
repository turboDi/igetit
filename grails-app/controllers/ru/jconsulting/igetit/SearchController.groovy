package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class SearchController {

    static allowedMethods = [buys: "GET", persons: "GET"]

    def searchService

    def buys() {
        render searchService.searchBuys(params) as JSON
    }

    def persons() {
        render searchService.searchPersons(params) as JSON
    }
}
