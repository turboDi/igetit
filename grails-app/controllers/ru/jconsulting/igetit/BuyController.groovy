package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import ru.jconsulting.igetit.auth.User

@Secured(['ROLE_USER'])
class BuyController extends RestfulController<Buy> {

    static responseFormats = ['json']

    def springSecurityService

    BuyController() {
        super(Buy)
    }

    @Override
    protected List<Buy> listAllResources(Map params) {
        if (params.userId) {
            def uid = params.userId
            Buy.where {owner.id == uid}.list(params)
        } else {
            super.listAllResources(params)
        }
    }

    @Override
    protected Buy createResource(Map params) {
        Buy buy = super.createResource(params) as Buy
        User currentUser = springSecurityService.getCurrentUser() as User
        buy.owner = currentUser
        buy.created = new Date()
        buy
    }
}
