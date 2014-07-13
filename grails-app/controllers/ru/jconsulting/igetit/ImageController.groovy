package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class ImageController {

    def storage

    def upload() {
        def folder = storage.createFolder(UUID.randomUUID().toString())
        def file = storage.uploadFile(params.file, folder.getId())
        Image image = new Image(fileId : file.getId(), folderId : folder.getId(), filename : file.getOriginalFilename())
        render image as JSON
    }
}
