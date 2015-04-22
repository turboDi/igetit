package ru.jconsulting.igetit

import grails.converters.JSON

class SearchController {

    def searchService

    def buys() {
        render searchService.searchBuys(params) as JSON
    }

    def persons() {
        render searchService.searchPersons(params) as JSON
    }
}
