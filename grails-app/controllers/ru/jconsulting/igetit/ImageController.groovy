package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Secured(['ROLE_USER'])
class ImageController extends RestfulController<Image> {

    ImageController() {
        super(Image)
    }

    @Transactional(readOnly = true)
    def index(Integer max) {
        def buy = Buy.get(params.buyId)
        if (buy == null) {
            render status: NOT_FOUND
            return
        }

        respond buy.images
    }

    @Transactional
    def save() {
        def image = new Image(params)

        image.validate()
        if (image.hasErrors()) {
            respond image.errors
            return
        }

        def buy = Buy.get(params.buyId)
        if (buy == null) {
            render status: NOT_FOUND
            return
        }

        buy.addToImages(image)

        buy.save(flush: true)

        respond image, [status: CREATED]
    }

    @Transactional
    def delete() {
        def buy = Buy.get(params.buyId)
        def image = buy?.images?.find {it.id == params.id}
        if (buy == null || image == null) {
            render status: NOT_FOUND
            return
        }

        buy.removeFromImages(image as Image)

        render status: NO_CONTENT
    }
}
