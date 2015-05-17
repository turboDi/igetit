package ru.jconsulting.igetit

import grails.rest.RestfulController

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 17.05.2015
 */
abstract class IGetItRestfulController<T> extends RestfulController<T> {

    IGetItRestfulController(Class<T> resource) {
        super(resource)
    }

    protected List getExcludedBindParams() {
        ['dateCreated']
    }

    @Override
    protected Map getParametersToBind() {
        Map map = super.getParametersToBind()
        getExcludedBindParams().each { param ->
            map.remove(param)
        }
        map
    }

}
