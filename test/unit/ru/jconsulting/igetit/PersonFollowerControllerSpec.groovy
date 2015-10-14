package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(PersonFollowerController)
@Mock([Person, PersonFollower])
class PersonFollowerControllerSpec extends Specification {

    Person user1, user2, user3, user4
    PersonFollower pf

    def setup() {
        Person.metaClass.encodePassword { -> }
        user1 = new Person(username: 'user1@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user2 = new Person(username: 'user2@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user3 = new Person(username: 'user3@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        user4 = new Person(username: 'user4@ww.ww', fullName: 'FIO', password: 'pwd').save(flush: true, failOnError: true)
        PersonFollower.create user1, user2, true
        pf = PersonFollower.create user1, user3, true
        PersonFollower pf1 = new PersonFollower(person: user1, follower: user4)
        pf1.deleted = true
        pf1.save(flush: true)
        controller.metaClass.getAuthenticatedUser = { -> user3 }
        controller.params.format = 'json'
    }

    void "test get all followers"() {
        given:
        params.personId = user1.id
        when:
        controller.index()
        then:
        response.json*.follower*.id == [user2.id, user3.id]
    }

    void "test add to followers"() {
        given:
        params.personId = user2.id
        when:
        controller.save()
        then:
        response.json.person.id == user2.id && response.json.follower.id == user3.id
        PersonFollower.countByPersonAndFollower(user2, user3) == 1
    }

    void "test add to undefined followers"() {
        given:
        params.personId = 100500
        when:
        controller.save()
        then:
        response.status == 422
        response.json.errors.size() == 1
    }

    void "test stop following"() {
        given:
        params.id = pf.id
        when:
        controller.delete()
        then:
        response.status == 204
        PersonFollower.countByPersonAndFollowerAndDeleted(user1, user3, false) == 0
        PersonFollower.countByPersonAndDeleted(user1, false) == 1
    }

    void "test query without personId parameter"() {
        when:
        controller.index()
        then:
        thrown(IllegalStateException)
    }

    void "test query deleted personFollower"() {
        given:
        controller.metaClass.getAuthenticatedUser = { -> user4 }
        params.personId = user1.id
        when:
        controller.delete()
        then:
        response.status == 404
    }
}
