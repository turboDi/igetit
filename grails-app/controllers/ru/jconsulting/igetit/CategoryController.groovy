package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

@Secured(['ROLE_USER'])
class CategoryController extends RestfulController<Category> {

    static responseFormats = ['json']

    CategoryController() {
        super(Category)
    }

    @Override
    protected Map getParametersToBind() {
        request.JSON as Map
    }
}
