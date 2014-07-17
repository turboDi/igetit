package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class ImageController {

    def storage

    def upload() {
        log.debug("File '$params.file.originalFilename' of $params.file.size bytes received")
        def folder = storage.createFolder(UUID.randomUUID().toString())
        def file = storage.uploadFile(params.file, folder.getId())
        Image image = new Image(fileId : file.getId(), folderId : folder.getId(), filename : file.getOriginalFilename())
        log.debug("New image uploaded to ${storage.getURL(image)}")
        render image as JSON
    }
}
