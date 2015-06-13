package ru.jconsulting.igetit

import spock.lang.Specification

class SearchServiceSpec extends Specification {

    def searchService

    Buy buy1, buy2, buy3
    Person p1, p2, p3
    Category category
    String buyName = UUID.randomUUID()
    String placeId = UUID.randomUUID()

    def setup() {
        City city = new City(placeId: placeId, description: '123')
        category = new Category(name: 'Test').save(flush: true)
        p1 = new Person(username: 'p1@ww.ww', city: city, fullName: 'FIO', password: '123').save(flush: true, failOnError: true)
        p2 = new Person(username: 'p2@ww.ww', fullName: 'FIO', password: '123').save(flush: true, failOnError: true)
        p3 = new Person(username: 'p3@ww.ww', fullName: 'FIO', password: '123').save(flush: true, failOnError: true)
        buy1 = new Buy(name: buyName, owner: p1, category: category).save(flush: true, failOnError: true)
        buy2 = new Buy(name: buyName, owner: p2, category: category).save(flush: true, failOnError: true)
        buy3 = new Buy(name: buyName, owner: p3).save(flush: true, failOnError: true)

    }

    void "test search buy"() {
        when:
        def buysByNameAndCity = searchService.searchBuys(term: buyName, placeId: placeId)
        def buysByNameAndCategory = searchService.searchBuys(term: buyName, categoryId: category.id, sort: 'owner.username')
        then:
        [buy1] == buysByNameAndCity
        [buy1, buy2] == buysByNameAndCategory
    }

    void "test search person"() {
        when:
        def personsByNameAndCity = searchService.searchPersons(term: 'FI', placeId: placeId)
        def personsByNameAndCategory = searchService.searchPersons(term: 'FI', categoryId: category.id, sort: 'username')
        then:
        [p1] == personsByNameAndCity
        [p1, p2] == personsByNameAndCategory
    }
}
