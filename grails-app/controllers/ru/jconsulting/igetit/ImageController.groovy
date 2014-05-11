package ru.jconsulting.igetit

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
class ImageController {

    def googleDriveService

    def upload() {
        def folder = googleDriveService.createFolder(UUID.randomUUID().toString())
        def file = googleDriveService.uploadFile(params.file, folder.getId())
        Image image = new Image(fileId : file.getId(), folderId : folder.getId(), filename : file.getOriginalFilename())
        render image as JSON
    }
}
