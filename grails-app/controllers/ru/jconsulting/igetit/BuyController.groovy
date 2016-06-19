package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.AccessDeniedException

import static grails.plugin.springsecurity.SpringSecurityUtils.ifNotGranted

@Secured(['ROLE_USER', 'ROLE_ADMIN'])
class BuyController extends IGetItRestfulController<Buy> {

    BuyController() {
        super(Buy)
    }

    @Override
    protected List<Buy> listAllResources(Map params) {
        if (params.personId) {
            def person = 'me' == params.personId ? getAuthenticatedUser() : Person.get(params.personId)
            if (!person) {
                throw new NotFoundException("There is no such person with id=${params.personId}")
            }
            Buy.findAllByOwnerAndDeleted(person, false, params)
        } else {
            super.listAllResources(params)
        }
    }

    @Override
    protected Buy createResource(Map params) {
        Buy buy = super.createResource(params) as Buy
        buy.owner = getAuthenticatedUser() as Person
        if (!buy.city) {
            log.debug("User '$buy.owner' hasn't specified buy's city")
            buy.city = buy.owner.city
        }
        log.debug("User '$buy.owner' is about to create new buy: $buy.name")
        buy
    }

    @Override
    protected Buy queryForResource(Serializable id) {
        Buy buy = super.queryForResource(id) as Buy
        def currentUser = getAuthenticatedUser()
        if (request.method != 'GET' && buy && ifNotGranted('ROLE_ADMIN') && !buy.owner.equals(currentUser)) {
            log.error("Invalid $request.method attempt by '$currentUser' of '$buy'")
            throw new AccessDeniedException('This buy belongs to another user')
        }
        buy
    }
}
