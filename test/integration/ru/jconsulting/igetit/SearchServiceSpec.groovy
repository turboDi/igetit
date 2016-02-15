package ru.jconsulting.igetit

import spock.lang.Specification

class SearchServiceSpec extends Specification {

    def searchService

    Buy buy1, buy2, buy3
    Person p1, p2, p3
    Category category
    Shop shop
    String buyName = UUID.randomUUID()
    String placeId = UUID.randomUUID()

    def setup() {
        City city = new City(placeId: placeId, description: '123')
        shop = new Shop(name: '123', city: city, sourceId: '123').save(failOnError: true)
        category = new Category(name: 'Test').save()
        p1 = new Person(username: 'p1@ww.ww', fullName: 'FIO', password: '123').save()
        p2 = new Person(username: 'p2@ww.ww', city: city, fullName: 'FIO', password: '123').save()
        p3 = new Person(username: 'p3@ww.ww', fullName: 'FIO', password: '123').save()
        buy1 = new Buy(name: buyName, owner: p1, category: category, city: city).save()
        buy2 = new Buy(name: buyName, owner: p2, category: category).save()
        buy3 = new Buy(name: buyName, owner: p3, shop: shop).save()
        new Buy(owner: p1, category: category).save()
        new Buy(owner: p1, category: category).save()
        new Buy(owner: p1, category: category).save()
    }

    void "test search buy"() {
        when:
        def buysByNameAndCity = searchService.searchBuys(term: buyName, placeId: placeId)
        def buysByNameAndCategory = searchService.searchBuys(term: buyName, categoryId: category.id, sort: 'owner.username')
        def buysByNameAndShop = searchService.searchBuys(term: buyName, shopName: '2')
        then:
        [buy1] == buysByNameAndCity
        [buy1, buy2] == buysByNameAndCategory
        [buy3] == buysByNameAndShop
    }

    void "test search person"() {
        when:
        def personsByNameAndCity = searchService.searchPersons(term: 'FI', placeId: placeId)
        def personsByNameAndCategory = searchService.searchPersons(term: 'FI', categoryId: category.id, sort: 'username', max: 3)
        then:
        [p2] == personsByNameAndCity
        [p1, p2] == personsByNameAndCategory
    }

    void "test search shop"() {
        when:
        def shopsByNameAndCity = searchService.searchShops(term: '23', placeId: placeId)
        then:
        [shop] == shopsByNameAndCity
    }
}
