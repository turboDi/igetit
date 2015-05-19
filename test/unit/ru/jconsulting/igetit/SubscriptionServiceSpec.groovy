package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(SubscriptionService)
@Mock([Person, PersonFollower, Price, Buy, Event])
class SubscriptionServiceSpec extends Specification {

    Person user1, user2

    def setup() {
        Person.metaClass.encodePassword { -> }
        user1 = new Person(username: 'user1@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user2 = new Person(username: 'user2@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        Price p = new Price(value: new BigDecimal(1), currency: Currency.getInstance('USD'))
        new Buy(name: 'buy1', owner: user1, price: p).save(flush: true, failOnError: true)
        new Buy(name: 'buy2', owner: user2, price: p).save(flush: true, failOnError: true)
        new Event(effector: user1, initiator: user2, text: '123', refId: 1, type: '123', args: '123').save(flush: true, failOnError: true)
        new Event(effector: user1, initiator: user2, text: '123', refId: 1, type: '123', args: '123').save(flush: true, failOnError: true)
    }

    void "test tape"() {
        given:
        PersonFollower.create(user2, user1, true)
        when:
        def user1Tape = service.tape(user1, [:])
        def user2Tape = service.tape(user2, [:])
        then:
        user1Tape.size() == 2
        user2Tape.size() == 1
    }

    void "test events"() {
        when:
        def events = service.events(user1, [:])
        then:
        events.size() == 2
    }
}
