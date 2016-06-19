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
        if (params.personId) {
            def currentUser = getAuthenticatedUser()
            def person = 'me' == params.personId ? currentUser : Person.get(params.personId)
            if (person) {
                JSON.use('personFollowed') {
                    if (person == currentUser) {
                        render PersonFollower.findAllByFollower(person, params) as JSON
                    } else {
                        render PersonFollower.findAllByFollowerAndDeleted(person, false, params) as JSON
                    }
                }
            } else {
                render status: NOT_FOUND
            }
        } else {
            throw new IllegalStateException("Followed list requested without required 'personId' parameter")
        }
    }
}
