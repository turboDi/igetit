package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

@Secured(['ROLE_USER'])
class PersonFavoriteController extends RestfulController<PersonFavorite> {

    PersonFavoriteController() {
        super(PersonFavorite)
    }

    @Override
    protected List<PersonFavorite> listAllResources(Map params) {
        PersonFavorite.findAllByPerson(getAuthenticatedUser() as Person)
    }

    @Override
    protected PersonFavorite createResource(Map params) {
        Buy buy = Buy.get(params.buyId as Serializable)
        if (!buy) {
            log.error("Couldn't find buy with id '$params.buyId'")
        }
        new PersonFavorite(person: getAuthenticatedUser(), buy: buy)
    }

    @Override
    protected PersonFavorite queryForResource(Serializable id) {
        PersonFavorite.where {
            person == getAuthenticatedUser()
            buy.id == id
        }.get()
    }
}
