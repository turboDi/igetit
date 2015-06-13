package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class PersonFollowerController extends IGetItRestfulController<PersonFollower> {

    PersonFollowerController() {
        super(PersonFollower)
    }

    @Override
    protected List<PersonFollower> listAllResources(Map params) {
        if (params.personId) {
            def pid = params.personId
            PersonFollower.where { person.id == pid && deleted == false }.list(params)
        } else {
            throw new IllegalStateException("Followers list requested without required 'personId' parameter")
        }
    }

    @Override
    protected PersonFollower createResource(Map params) {
        Person person = Person.get(params.personId as Serializable)
        if (!person) {
            log.error("Couldn't find person with id '$params.personId'")
        }
        new PersonFollower(person: person, follower: getAuthenticatedUser())
    }

    @Override
    protected PersonFollower queryForResource(Serializable id) {
        if (params.personId) {
            def pid = params.personId
            PersonFollower.where {
                person.id == pid
                follower == getAuthenticatedUser()
                deleted == false
            }.get()
        } else {
            throw new IllegalStateException("Follower query requested without required 'personId' parameter")
        }
    }
}
