package ru.jconsulting.igetit

import grails.transaction.Transactional

@Transactional(readOnly = true)
class SearchService {

    def searchBuys(Map params) {
        Buy.createCriteria().list(params) {
            eq 'deleted', false
            if (params.term) {
                ilike 'name', "%$params.term%"
            }
            if (params.categoryId) {
                eq 'category.id', params.categoryId
            }
            if (params.placeId) {
                delegate.owner {
                    city {
                        eq 'placeId', params.placeId
                    }
                }
            }
        }
    }

    def searchPersons(Map params) {
        Person.createCriteria().list(params) {
            eq 'deleted', false
            if (params.term) {
                ilike 'fullName', "%$params.term%"
            }
            if (params.placeId) {
                eq 'city.placeId', params.placeId
            }
            if (params.categoryId) {
                createAlias 'buys', 'b'
                eq 'b.category.id', params.categoryId
            }
        }
    }
}
