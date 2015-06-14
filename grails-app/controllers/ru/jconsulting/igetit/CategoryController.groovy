package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class CategoryController extends IGetItRestfulController<Category> {

    CategoryController() {
        super(Category)
    }

    @Override
    protected List<Category> listAllResources(Map params) {
        Category.list(params)
    }
}
