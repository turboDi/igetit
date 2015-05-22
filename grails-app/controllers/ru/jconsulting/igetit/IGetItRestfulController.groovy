package ru.jconsulting.igetit

import grails.rest.RestfulController
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.NO_CONTENT
/**
 *
 *
 * @author Dmitriy Borisov
 * @created 17.05.2015
 */
abstract class IGetItRestfulController<T> extends RestfulController<T> {

    final List excludedBindParams

    IGetItRestfulController(Class<T> resource) {
        this(resource, [])
    }

    IGetItRestfulController(Class<T> resource, List excludedBindParams) {
        super(resource)
        this.excludedBindParams = excludedBindParams
    }

    @Transactional
    def delete() {
        def instance = queryForResource(params.id as Serializable)
        if (instance == null) {
            notFound()
            return
        }
        doDelete(instance)

        render status: NO_CONTENT
    }

    @Override
    protected List<T> listAllResources(Map params) {
        resource.where { eq 'deleted', false }.list(params)
    }

    @Override
    protected T queryForResource(Serializable id) {
        resource.findNotDeletedById(id)
    }

    @Override
    protected Map getParametersToBind() {
        Map map = super.getParametersToBind()
        excludedBindParams.each { param ->
            map.remove(param)
        }
        map
    }

    protected void doDelete(T instance) {
        instance.deleted = true
        instance.save flush:true
    }

}
