package ru.jconsulting.igetit

import spock.lang.Specification

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 20.04.2015
 */
class BuySpec extends Specification {

    Buy buy1, buy2, buy3
    String buyName = UUID.randomUUID()

    def setup() {
        IGetItPersistenceEventListener.metaClass.saveNewEvent = { Event e -> }
        Person p1 = new Person(username: 'p1@ww.ww', fullName: 'FIO', password: '123').save(flush: true, failOnError: true)
        Person p2 = new Person(username: 'p2@ww.ww', fullName: 'FIO', password: '123').save(flush: true, failOnError: true)
        Person p3 = new Person(username: 'p3@ww.ww', fullName: 'FIO', password: '123').save(flush: true, failOnError: true)
        buy1 = new Buy(name: buyName, owner: p1).save(flush: true, failOnError: true)
        buy2 = new Buy(name: buyName, owner: p2).save(flush: true, failOnError: true)
        buy3 = new Buy(name: buyName, owner: p3).save(flush: true, failOnError: true)
        buy1.like(p2)
        buy2.like(p1)
        buy2.like(p3)
    }

    void "test order by likes count"() {
        when:
        def buys = Buy.where {name == buyName}.list(sort: 'likesCount', order: 'desc')
        then:
        buys.size() == 3
        buys[0] == buy2
        buys[1] == buy1
        buys[2] == buy3
    }
}
