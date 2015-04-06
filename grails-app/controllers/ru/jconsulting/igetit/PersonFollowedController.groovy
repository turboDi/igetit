package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NOT_FOUND

@Transactional(readOnly = true)
@Secured(['ROLE_USER'])
class PersonFollowedController {

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def pid = params.personId as Serializable
        if (!pid) {
            log.error("Followed list requested without required 'personId' parameter")
            render status: NOT_FOUND
            return
        }
        if (Person.get(pid)) {
            JSON.use('personFollowed') {
                render PersonFollower.where {follower.id == pid}.list(params) as JSON
            }
        } else {
            render status: NOT_FOUND
        }
    }
}
