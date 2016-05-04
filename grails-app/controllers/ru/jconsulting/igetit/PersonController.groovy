package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.AccessDeniedException

import static org.springframework.http.HttpStatus.OK

@Secured(['ROLE_USER'])
class PersonController extends IGetItRestfulController<Person> {

    def accountService
    def accessTokenJsonRenderer

    PersonController() {
        super(Person)
    }

    @Override
    @Secured(['permitAll'])
    def save() {
        def person = new Person(params)
        def token = person.oAuthProvider ? accountService.tryReAuthenticate(person) : null
        if (!token) {
            person.validate()

            if (person.hasErrors()) {
                respond person.errors, view:'edit' // STATUS CODE 422
                return
            }

            token = accountService.register(person)
        }
        render JSON.parse(accessTokenJsonRenderer.generateJson(token)) as JSON
    }

    @Override
    def update() {
        Person person = queryForResource(params.id as Serializable)
        if (person == null) {
            notFound()
            return
        }

        bindData(person, getParametersToBind(), [exclude: ['oAuthProvider', 'username']])

        person.validate()
        if (person.hasErrors()) {
            respond person.errors, view:'edit' // STATUS CODE 422
            return
        }

        accountService.update(person)

        respond person, [status: OK]
    }

    @Override
    protected Person queryForResource(Serializable id) {
        def currentUser = getAuthenticatedUser()
        if ('me' == id) {
            return currentUser
        }
        Person person = super.queryForResource(id) as Person
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
