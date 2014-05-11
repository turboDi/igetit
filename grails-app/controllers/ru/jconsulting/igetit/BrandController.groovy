package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

@Secured(['ROLE_USER'])
class BrandController extends RestfulController<Brand> {

    static responseFormats = ['json']

    BrandController() {
        super(Brand)
    }
}