package ru.jconsulting.igetit

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.Specification

@TestFor(BuyController)
@TestMixin(DomainClassUnitTestMixin)
class BuyControllerSpec extends Specification {

    Person user1
    Person user2

    def setup() {
        mockDomains(Person, Price)
        Person.metaClass.encodePassword { -> }
        user1 = new Person(username: 'user1', password: 'pwd').save(flush: true)
        user2 = new Person(username: 'user2', password: 'pwd').save(flush: true)
        Price p = new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))
        mockDomain(Buy, [
                [name: 'buy1', owner: user1, price: p, created: new Date()],
                [name: 'buy2', owner: user2, price: p, created: new Date()]
        ])
        assert Buy.count() == 2
        def springSecurityServiceMock = mockFor(SpringSecurityService)
        springSecurityServiceMock.demand.getCurrentUser { -> user2 }
        controller.springSecurityService = springSecurityServiceMock.createMock()
    }

    void "test list all buys"() {
        when:
        controller.index()
        then:
        response.json.size() == 2
    }

    void "test specified user's buys"() {
        given:
        params.personId = user1.id
        when:
        controller.index()
        then:
        response.json.size() == 1
        response.json.name == ['buy1']
    }

    void "test current user buy something"() {
        given:
        params.name = 'buy3'
        params.price = new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))
        when:
        controller.save()
        then:
        response.json.name == 'buy3'
        response.json.owner.id == user2.id
        Buy.count() == 3
    }
}
