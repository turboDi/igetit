package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

@Secured(['ROLE_USER'])
class CategoryController extends RestfulController<Category> {

    CategoryController() {
        super(Category)
    }
}
