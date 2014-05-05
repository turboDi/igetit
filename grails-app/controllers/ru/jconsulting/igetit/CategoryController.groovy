package ru.jconsulting.igetit

import grails.rest.RestfulController

class CategoryController extends RestfulController<Category> {

    static responseFormats = ['json']

    CategoryController() {
        super(Category)
    }
}
