package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
@Mock([Person, PersonFollower])
class PersonFollowerSpec extends Specification {

    Person user1, user2, user3

    def setup() {
        Person.metaClass.encodePassword { -> }
        Person.metaClass.accountService = [ isPasswordValid: { p, e -> true } ]
        user1 = new Person(username: 'user1@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user2 = new Person(username: 'user2@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user3 = new Person(username: 'user3@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
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

    void "test self following"() {
        when:
        def pf = new PersonFollower(person: user1, follower: user1)
        then:
        !pf.validate()
    }

    void "test duplicate following"() {
        setup:
        mockForConstraintsTests(PersonFollower, [new PersonFollower(person: user1, follower: user2)])
        when:
        def pf = new PersonFollower(person: user1, follower: user2)
        then:
        !pf.validate()
    }
}
