package ru.jconsulting.igetit.marshallers

import grails.plugin.springsecurity.SpringSecurityService
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

    static def marshallPerson(person) {
        [
                id : person.id,
                name : person.fullName,
                avatar : person.avatar
        ]
    }
}
