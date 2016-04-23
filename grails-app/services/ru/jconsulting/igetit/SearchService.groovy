package ru.jconsulting.igetit

import grails.transaction.Transactional

@Transactional(readOnly = true)
class SearchService {

    def searchBuys(Map params) {
        def sort
        if (params.sort instanceof Map) {
            sort = params.remove('sort')
        }
        Buy.createCriteria().list(params) {
            if (sort) {
                for (e in sort) {
                    order(e.key, e.value)
                }
            }
            eq 'deleted', false
            if (params.term) {
                textSearch 'name', params.term
            }
            if (params.categoryId) {
                eq 'category.id', params.categoryId
            }
            if (params.placeId) {
                eq 'city.placeId', params.placeId
            }
            if (params.shopName) {
                shop {
                    ilike 'name', "%$params.shopName%"
                }
            }
        }
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def searchPersons(Map params) {
        def sort
        if (params.sort instanceof Map) {
            sort = params.remove('sort')
        }
        def ids = Person.createCriteria().list(params) {
            if (sort) {
                for (e in sort) {
                    order(e.key, e.value)
                }
            }
            projections {
                groupProperty('id')
                if (sort) {
                    for (e in sort) {
                        groupProperty(e.key)
                    }
                } else if (params.sort) {
                    groupProperty(params.sort)
                }
            }
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
        Person.getAll(sort || params.sort ? ids.collect { it[0] } : ids)
    }

    def searchShops(Map params) {
        def sort
        if (params.sort instanceof Map) {
            sort = params.remove('sort')
        }
        Shop.createCriteria().list(params) {
            if (sort) {
                for (e in sort) {
                    order(e.key, e.value)
                }
            }
            eq 'deleted', false
            if (params.term) {
                ilike 'name', "%$params.term%"
            }
            if (params.placeId) {
                eq 'city.placeId', params.placeId
            }
        }
    }
}
