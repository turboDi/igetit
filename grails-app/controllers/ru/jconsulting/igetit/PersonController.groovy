package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.AccessDeniedException

@Secured(['ROLE_USER'])
class PersonController extends IGetItRestfulController<Person> {

    PersonController() {
        super(Person, ['username', 'confirmToken', 'oAuthProvider'])
    }

    @Override
    protected Person queryForResource(Serializable id) {
        Person person = super.queryForResource(id) as Person
        def currentUser = getAuthenticatedUser()
        if (request.method != 'GET' && person && !person.equals(currentUser)) {
            log.error("Invalid $request.method attempt by '$currentUser' of '$person'")
            throw new AccessDeniedException('You can\'t modify another user\'s info')
        }
        person
    }

    @Override
    protected void doDelete(Person instance) {
        instance.enabled = false
        super.doDelete(instance)
    }
}
