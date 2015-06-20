package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class SearchController {

    static namespace = "v1"
    static allowedMethods = [buys: "GET", persons: "GET"]

    def searchService

    def buys() {
        params.categoryId = params.long('categoryId')
        render searchService.searchBuys(params) as JSON
    }

    def persons() {
        params.categoryId = params.long('categoryId')
        render searchService.searchPersons(params) as JSON
    }
}
