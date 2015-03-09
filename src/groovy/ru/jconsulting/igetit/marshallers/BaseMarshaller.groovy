package ru.jconsulting.igetit.marshallers

import grails.plugin.springsecurity.SpringSecurityService
import groovy.time.TimeCategory
import org.springframework.beans.factory.annotation.Autowired
import ru.jconsulting.igetit.Person

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 13.12.14 23:32
 */
abstract class BaseMarshaller {

    @Autowired
    private SpringSecurityService springSecurityService

    Person currentPerson() {
        springSecurityService.getCurrentUser() as Person
    }

    def marshallPerson(person) {
        [
                id : person.id,
                name : person.username,
                avatar : person.avatar
        ]
    }

    def marshallDate(date) {
        use(TimeCategory) {
            def duration = new Date() - date
            duration.days >= 365 ?
                    date.format("dd MMM yyyy") :
            duration.days ?
                    date.format("dd MMM 'at' hh:mm") :
            duration.hours ?
                    date.format("hh:mm") :
            duration.minutes ?
                    "$duration.minutes minutes ago" :
            "just now"
        }
    }
}
