package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.AccessDeniedException

import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

@Secured(['ROLE_USER'])
class PersonController extends IGetItRestfulController<Person> {

    def accountService
    def restAuthenticationTokenJsonRenderer

    PersonController() {
        super(Person, ['username', 'confirmToken', 'oAuthProvider'])
    }

    @Override
    @Secured(['permitAll'])
    def save() {
        def token = null
        if (!params.oAuthProvider) {
            params.email = params.username
        } else {
            params.password = 'N/A'
            token = accountService.tryReAuthenticate(params.username, params.oAuthProvider)
        }
        if (!token) {
            def p = new Person(params)
            p.validate()
            if (p.hasErrors()) {
                response.status = UNPROCESSABLE_ENTITY.value()
                render p.errors as JSON
                return
            }
            token = accountService.register(p)
        }
        render JSON.parse(restAuthenticationTokenJsonRenderer.generateJson(token)) as JSON
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
