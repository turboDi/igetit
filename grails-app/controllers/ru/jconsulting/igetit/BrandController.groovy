package ru.jconsulting.igetit

import grails.rest.RestfulController

class BrandController extends RestfulController<Brand> {

    static responseFormats = ['json']

    BrandController() {
        super(Brand)
    }
}
