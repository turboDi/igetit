package ru.jconsulting.igetit

import grails.rest.RestfulController

class BuyController extends RestfulController<Buy> {

    static responseFormats = ['json']

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
        if (params.personId) {
            buy.owner = Person.get(params.personId as Serializable)
        }
        buy
    }
}
