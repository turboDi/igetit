package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.AccessDeniedException

@Secured(['ROLE_USER'])
class ShopController extends IGetItRestfulController<Shop> {

    def searchService

    ShopController() {
        super(Shop)
    }

    @Override
    protected List<Shop> listAllResources(Map params) {
        if (params.cityId) {
            City city = City.get(params.cityId)
            if (!city) {
                throw new NotFoundException("There is no such city with id=${params.cityId}")
            }
            params.placeId = city.placeId
        }
        searchService.searchShops(params)
    }

    @Override
    protected Shop createResource(Map params) {
        if (!params.eshop) {
            throw new AccessDeniedException('You have no rights to create offline shop')
        }
        super.createResource(params)
    }
}
