package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class CityController extends IGetItRestfulController<City> {

    CityController() {
        super(City)
    }

    @Override
    protected List<City> listAllResources(Map params) {
        City.list(params)
    }

    @Override
    protected City queryForResource(Serializable id) {
        City.get(id)
    }
}
