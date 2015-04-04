package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*

@Secured(['ROLE_USER'])
class StorageController {

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
                s: params.int('s') ?: 128,
                m: params.int('m') ?: 256,
                l: params.int('l') ?: 512
        ]
    }
}
