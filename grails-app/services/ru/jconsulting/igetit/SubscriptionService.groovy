package ru.jconsulting.igetit

import grails.transaction.Transactional

@Transactional(readOnly = true)
class SubscriptionService {

    def tape(Person person, Map params) {
        List<Person> owners = PersonFollower.findAllNotDeletedByFollower(person)*.person
        Buy.findAllNotDeletedByOwnerInList(owners << person as List, params)
    }

    def events(Person person, Map params) {
        Event.findAllByEffector(person, params)
    }
}
