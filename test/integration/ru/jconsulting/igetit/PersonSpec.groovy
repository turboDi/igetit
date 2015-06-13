package ru.jconsulting.igetit

import spock.lang.Specification

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 21.04.2015
 */
class PersonSpec extends Specification {

    Person p1, p2, p3
    UUID personName = UUID.randomUUID()

    void setup() {
        EventProducer.metaClass.saveNewEvent = { Event e -> }
        p1 = new Person(username: 'p1@ww.ww', fullName: personName, password: '123').save(flush: true, failOnError: true)
        p2 = new Person(username: 'p2@ww.ww', fullName: personName, password: '123').save(flush: true, failOnError: true)
        p3 = new Person(username: 'p3@ww.ww', fullName: personName, password: '123').save(flush: true, failOnError: true)

        PersonFollower.create p2, p1, true
        PersonFollower.create p2, p3, true
        PersonFollower.create p3, p1, true
    }

    void "test order by followers count"() {
        when:
        def persons = Person.where { fullName == personName}.list(sort: 'followersCount', order: 'desc')
        then:
        persons.size() == 3
        persons[0] == p2
        persons[1] == p3
        persons[2] == p1
    }

}
