package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

@Secured(['ROLE_USER'])
class PersonController extends RestfulController<Person> {

    static responseFormats = ['json']

    def beforeInterceptor = [action: this.&generateConfirmToken, only: 'save']
    def afterInterceptor = [action: this.&sendConfirm, only: 'save']

    PersonController() {
        super(Person)
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
