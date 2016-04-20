package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(PersonFavoriteController)
@Mock([Person, PersonFavorite, Buy, Price])
class PersonFavoriteControllerSpec extends Specification {

    Person user1, user2
    Buy buy1, buy2
    PersonFavorite pf

    def setup() {
        Person.metaClass.encodePassword { -> }
        user1 = new Person(username: 'user1@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user2 = new Person(username: 'user2@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        buy1 = new Buy(name: 'buy', owner: user1, price: new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))).save(flush: true, failOnError: true)
        buy2 = new Buy(name: 'buy', owner: user2, price: new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))).save(flush: true, failOnError: true)
        pf = PersonFavorite.create user1, buy2, true
        PersonFavorite.create user2, buy1, true
        controller.metaClass.getAuthenticatedUser = { -> user1 }
        controller.params.format = 'json'
    }

    void "test get all favorites"() {
        when:
        controller.index()
        then:
        response.json*.buy*.id == [buy2.id]
    }

    void "test add to favorites"() {
        given:
        params.buyId = buy1.id
        when:
        controller.save()
        then:
        response.json.person.id == user1.id && response.json.buy.id == buy1.id
        PersonFavorite.countByPersonAndBuy(user1, buy1) == 1
    }

    void "test add undefined to favorites"() {
        given:
        params.buyId = 100500
        when:
        controller.save()
        then:
        response.status == 422
        response.json.errors.size() == 1
    }

    void "test remove from favorites"() {
        given:
        params.id = pf.id
        when:
        controller.delete()
        then:
        response.status == 204
        PersonFavorite.countByPerson(user1) == 0
    }
}
