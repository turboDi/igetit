package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.AccessDeniedException

@Secured(['ROLE_USER'])
class PersonController extends IGetItRestfulController<Person> {

    PersonController() {
        super(Person)
    }

    @Override
    protected Person queryForResource(Serializable id) {
        Person person = super.queryForResource(id) as Person
        def currentUser = getAuthenticatedUser()
        if (request.method != 'GET' && !person.equals(currentUser)) {
            log.error("Invalid $request.method attempt by '$currentUser' of '$person'")
            throw new AccessDeniedException('You can\'t modify another user\'s info')
        }
        person
    }

    @Override
    protected List getExcludedBindParams() {
        List excluded = super.getExcludedBindParams()
        excluded += ['username', 'lastActivity', 'enabled', 'accountExpired', 'accountLocked',
                     'passwordExpired', 'emailConfirmed', 'confirmToken', 'oAuthProvider']
        excluded
    }
}
