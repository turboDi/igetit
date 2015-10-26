package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
@Mock([Person, PersonFavorite, Buy, Price])
class PersonFavoriteSpec extends Specification {

    Person user
    Buy buy

    def setup() {
        Person.metaClass.encodePassword { -> }
        Person.metaClass.accountService = [ isPasswordValid: { p, e -> true } ]
        user = new Person(username: 'user1@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        buy = new Buy(name: 'buy', owner: user, price: new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))).save(flush: true, failOnError: true)
    }

    void "test duplicate favorite"() {
        setup:
        mockForConstraintsTests(PersonFavorite, [new PersonFavorite(person: user, buy: buy)])
        when:
        def pf = new PersonFavorite(person: user, buy: buy)
        then:
        !pf.validate()
    }
}
