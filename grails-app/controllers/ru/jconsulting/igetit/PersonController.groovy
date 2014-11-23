package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional

@Secured(['ROLE_USER'])
class PersonController extends RestfulController<Person> {

    static responseFormats = ['json']

    def springSecurityService

    def beforeInterceptor = [action: this.&generateConfirmToken, only: 'save']
    def afterInterceptor = [action: this.&sendConfirm, only: 'save']

    PersonController() {
        super(Person)
    }

    @Transactional
    def follow() {
        def person = queryForResource(params.id)
        if (person == null) {
            notFound()
            return
        }
        Person current = springSecurityService.getCurrentUser() as Person
        if (person == current) {
            throw new IllegalArgumentException("You can't follow yourself")
        }
        PersonFollower personFollower = PersonFollower.findByPersonAndFollower(person, current)
        if (!personFollower) {
            PersonFollower.create(person, current, true)
        } else {
            personFollower.delete(flush: true)
        }
        render "${PersonFollower.countByPerson(person)}"
    }

    private generateConfirmToken() {
        params.confirmToken = UUID.randomUUID()
    }

    private sendConfirm(model) {
        if (response.status == 201) {
            sendMail {
                to params.email
                subject "Welcome to IGetIt"
                html view: '/account/email', model: [p: params]
            }
        }
    }
}
