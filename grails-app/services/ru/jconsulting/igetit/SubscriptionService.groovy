package ru.jconsulting.igetit

import grails.transaction.Transactional

@Transactional(readOnly = true)
class SubscriptionService {

    def tape(Person person, Map params) {
        List<Person> owners = PersonFollower.findAllByFollower(person)*.person
        Buy.findAllByOwnerInList(owners << person as List, params)
    }

    def events(Person person, Map params) {
        Event.findAllByEffector(person, params)
    }
}
