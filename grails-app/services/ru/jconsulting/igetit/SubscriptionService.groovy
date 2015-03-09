package ru.jconsulting.igetit

import grails.transaction.Transactional

@Transactional
class SubscriptionService {

    def follow(Person person, Person follower) {
        if (follower == person) {
            throw new IllegalArgumentException("User can't follow himself")
        }
        PersonFollower personFollower = PersonFollower.findByPersonAndFollower(person, follower)
        if (!personFollower) {
            PersonFollower.create(person, follower)
        } else {
            personFollower.delete()
        }
    }

    @Transactional(readOnly = true)
    def tape(Person person, Map params) {
        List<Person> owners = PersonFollower.findAllByFollower(person)*.person
        Buy.findAllByOwnerInList(owners << person as List, params)
    }

    @Transactional(readOnly = true)
    def events(Person person, Map params) {
        Event.findByEffector(person, params)
    }
}
