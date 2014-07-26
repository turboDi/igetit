package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

@Secured(['ROLE_USER'])
class BuyController extends RestfulController<Buy> {

    static responseFormats = ['json']

    def springSecurityService

    BuyController() {
        super(Buy)
    }

    @Override
    protected List<Buy> listAllResources(Map params) {
        if (params.personId) {
            def pid = params.personId
            Buy.where {owner.id == pid}.list(params)
        } else {
            super.listAllResources(params)
        }
    }

    @Override
    protected Buy createResource(Map params) {
        Buy buy = super.createResource(params) as Buy
        Person currentUser = springSecurityService.getCurrentUser() as Person
        buy.owner = currentUser
        buy.created = new Date()
        log.debug("User '$currentUser.username' is about to create new buy: $buy.name")
        buy
    }
}
