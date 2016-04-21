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

    static namespace = "v1"

    IGetItRestfulController(Class<T> resource) {
        super(resource)
    }

    @Override
    @Transactional(readOnly = true)
    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def rs = listAllResources(params)
        if (rs.empty) {
            log.debug("No $resourceName found")
        }
        respond rs
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
        resource.findAllByDeleted(false, params)
    }

    @Override
    protected T queryForResource(Serializable id) {
        if (request.method == 'GET') {
            super.queryForResource(id)
        } else {
            resource.findNotDeletedById(id)
        }
    }

    protected void doDelete(T instance) {
        instance.deleted = true
        instance.save flush:true
    }

}
