package ru.jconsulting.igetit

import grails.test.mixin.Mock
import spock.lang.Specification

@Mock([Person, PersonFollower])
class PersonFollowerSpec extends Specification {

    Person user1, user2, user3

    def setup() {
        Person.metaClass.encodePassword { -> }
        user1 = new Person(username: 'user1', email: 'ww@ww.ww', password: 'pwd').save(flush: true, failOnError: true)
        user2 = new Person(username: 'user2', email: 'ww1@ww.ww', password: 'pwd').save(flush: true, failOnError: true)
        user3 = new Person(username: 'user3', email: 'ww2@ww.ww', password: 'pwd').save(flush: true, failOnError: true)
    }

    void "test following"() {
        when:
        PersonFollower.create user1, user2
        PersonFollower.create user1, user3
        PersonFollower.create user2, user1
        then:
        PersonFollower.countByPerson(user1) == 2
        PersonFollower.countByFollower(user1) == 1
        PersonFollower.findByFollower(user1).person == user2
    }
}
