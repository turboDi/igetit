package ru.jconsulting.igetit

import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import org.springframework.security.access.AccessDeniedException
import spock.lang.Specification

@TestFor(BuyController)
@TestMixin(DomainClassUnitTestMixin)
class BuyControllerSpec extends Specification {

    Person user1
    Person user2

    def setup() {
        mockDomains(Person, Price)
        Person.metaClass.encodePassword { -> }
        user1 = new Person(username: 'user1@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user2 = new Person(username: 'user2@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        Price p = new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))
        mockDomain(Buy, [
                [name: 'buy1', owner: user1, price: p],
                [name: 'buy2', owner: user2, price: p]
        ])
        assert Buy.count() == 2
        controller.metaClass.getAuthenticatedUser = { -> user2 }
        controller.params.format = 'json'
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
        params.price = [value: 1, currency: 'USD']
        when:
        controller.save()
        then:
        response.json.name == 'buy3'
        response.json.owner.id == user2.id
        Buy.count() == 3
    }

    void "test delete another's buy"() {
        given:
        Buy buy = Buy.findByOwner(user1)
        params.id = buy.id
        when:
        controller.delete()
        then:
        thrown(AccessDeniedException)
    }

    void "test update another's buy"() {
        given:
        Buy buy = Buy.findByOwner(user1)
        params.id = buy.id
        params.name = 'new buy name'
        when:
        controller.update()
        then:
        thrown(AccessDeniedException)
    }
}
