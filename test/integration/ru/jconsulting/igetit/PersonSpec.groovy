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
        p3 = new Person(username: 'test', fullName: personName, oAuthProvider: 'google').save(flush: true, failOnError: true)

        PersonFollower.create p2, p1, true
        PersonFollower.create p2, p3, true
        PersonFollower.create p3, p1, true
    }

    void "username should be email only for regular user"() {
        given:
        Person regular = new Person(username: 'test@ww.ww', fullName: personName, password: '123')
        Person oAuth = new Person(username: 'test2@ww.ww', fullName: personName, password: '123', oAuthProvider: 'test')
        assert regular.validate()
        assert oAuth.validate()
        when:
        regular.username = 'invalid'
        oAuth.username = 'invalid2'
        then:
        !regular.validate()
        oAuth.validate()
    }

    void "password is required only for regular user"() {
        given:
        Person regular = new Person(username: 'test@ww.ww', fullName: personName, password: '123')
        Person oAuth = new Person(username: 'test123', fullName: personName, password: '123', oAuthProvider: 'test')
        assert regular.validate()
        assert oAuth.validate()
        when:
        regular.password = null
        oAuth.password = null
        then:
        !regular.validate()
        oAuth.validate()
    }

    void "email confirmed flag should reset after field update"() {
        given:
        p1.emailConfirmed = true
        when:
        p1.email = 'new@ww.ww'
        p1.save(flush: true)
        then:
        !p1.emailConfirmed
    }

    void "test regular user insert"() {
        expect:
        p1.email == 'p1@ww.ww'
        p1.password != '123'
    }

    void "test oAuth user insert"() {
        expect:
        !p3.email
        p3.password == 'N/A'
    }

    void "test change password"() {
        when:
        p1.password = '345'
        p1.oldPassword = '123'
        then:
        p1.validate()
    }

    void "test change password to the same"() {
        when:
        p1.password = '123'
        p1.oldPassword = '123'
        then:
        !p1.validate()
    }

    void "test change password with invalid old one"() {
        when:
        p1.password = '345'
        p2.password = '345'
        p2.oldPassword = '333'
        then:
        !p1.validate()
        !p2.validate()
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
