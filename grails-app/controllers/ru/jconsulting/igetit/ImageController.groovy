package ru.jconsulting.igetit

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.springframework.security.access.AccessDeniedException

import static org.springframework.http.HttpStatus.*

@Secured(['ROLE_USER'])
class ImageController extends IGetItRestfulController<Image> {

    ImageController() {
        super(Image)
    }

    @Transactional(readOnly = true)
    def index(Integer max) {
        def buy = Buy.get(params.buyId as Serializable)
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

        def buy = Buy.get(params.buyId as Serializable)
        if (buy == null) {
            render status: NOT_FOUND
            return
        }
        checkPermissions(buy)

        buy.addToImages(image)

        buy.save(flush: true)

        respond image, [status: CREATED]
    }

    @Transactional
    def delete() {
        def buy = Buy.get(params.buyId as Serializable)
        def image = buy?.images?.find {it.id == params.id}
        if (buy == null || image == null) {
            render status: NOT_FOUND
            return
        }
        checkPermissions(buy)

        buy.removeFromImages(image as Image)

        render status: NO_CONTENT
    }

    private void checkPermissions(Buy buy){
        def currentUser = getAuthenticatedUser()
        if (!buy.owner.equals(currentUser)) {
            log.error("Invalid $request.method attempt by '$currentUser' of '$buy'")
            throw new AccessDeniedException('This buy belongs to another user')
        }
    }
}
