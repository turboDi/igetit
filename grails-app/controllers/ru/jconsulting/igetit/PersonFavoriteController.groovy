package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class PersonFavoriteController extends IGetItRestfulController<PersonFavorite> {

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

    @Override
    protected void doDelete(PersonFavorite instance) {
        instance.delete flush:true
    }
}
