package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.AccessDeniedException

@Secured(['ROLE_USER'])
class PersonFollowerController extends IGetItRestfulController<PersonFollower> {

    PersonFollowerController() {
        super(PersonFollower)
    }

    @Override
    protected List<PersonFollower> listAllResources(Map params) {
        if (params.personId) {
            def person = 'me' == params.personId ? getAuthenticatedUser() : Person.get(params.personId)
            if (!person) {
                throw new NotFoundException("There is no such person with id=${params.personId}")
            }
            PersonFollower.findAllByPersonAndDeleted(person, false, params)
        } else {
            throw new IllegalStateException("Followers list requested without required 'personId' parameter")
        }
    }

    @Override
    protected PersonFollower createResource(Map params) {
        Person person = 'me' == params.personId ? getAuthenticatedUser() : Person.get(params.personId)
        if (!person) {
            log.error("Couldn't find person with id '$params.personId'")
        }
        new PersonFollower(person: person, follower: getAuthenticatedUser())
    }

    @Override
    protected PersonFollower queryForResource(Serializable id) {
        PersonFollower pf = super.queryForResource(id) as PersonFollower
        def currentUser = getAuthenticatedUser()
        if (request.method != 'GET' && pf && !pf.follower.equals(currentUser)) {
            log.error("Invalid $request.method attempt by '$currentUser' of '$pf'")
            throw new AccessDeniedException('You can\'t operate other followers')
        }
        pf
    }
}
