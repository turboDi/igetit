package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND

@Transactional(readOnly = true)
@Secured(['ROLE_USER'])
class PersonFollowedController {

    static namespace = "v1"

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def pid = params.personId as Serializable
        if (!pid) {
            throw new IllegalStateException("Followed list requested without required 'personId' parameter")
        }
        def person = Person.get(pid)
        if (person) {
            JSON.use('personFollowed') {
                render PersonFollower.findAllByFollowerAndDeleted(person, false, params) as JSON
            }
        } else {
            render status: NOT_FOUND
        }
    }
}
