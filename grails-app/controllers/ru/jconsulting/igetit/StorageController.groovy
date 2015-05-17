package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.security.access.AccessDeniedException
import ru.jconsulting.igetit.image.Thumbnail

import static org.springframework.http.HttpStatus.*

@Secured(['permitAll'])
class StorageController {

    static allowedMethods = [upload: "POST", delete: "DELETE"]

    def storageService

    def upload() {
        if (!params.file) {
            missingParameter 'file'
            return
        }
        log.debug("File '$params.file.originalFilename' of $params.file.size bytes received")
        Image image = storageService.upload params.file, thumbnails()
        render image as JSON
    }

    def delete() {
        if (!params.folderId) {
            missingParameter 'folderId'
            return
        }
        if (Image.countByFolderId(params.folderId)) {
            throw new AccessDeniedException('This image is still referenced by another entity')
        }
        log.debug("Deleting images with folder '$params.folderId'")
        storageService.delete params.folderId
        render status: NO_CONTENT
    }

    private void missingParameter(def param) {
        render(status: UNPROCESSABLE_ENTITY, contentType: "application/json") {
            status = UNPROCESSABLE_ENTITY.value()
            message = "'$param' parameter expected"
        }
    }

    private Map thumbnails() {
        [
                s: new Thumbnail(size: params.int('s') ?: 128, x: params.int('x'), y: params.int('y'), width: params.int('width')),
                m: new Thumbnail(size: params.int('m') ?: 256, x: params.int('x'), y: params.int('y'), width: params.int('width')),
                l: new Thumbnail(size: params.int('l'))
        ]
    }
}
