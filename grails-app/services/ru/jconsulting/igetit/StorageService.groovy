package ru.jconsulting.igetit

import org.apache.commons.io.FilenameUtils
import org.springframework.web.multipart.MultipartFile

import static grails.async.Promises.tasks
import static ImageUtils.*

class StorageService {

    def storage

    def upload(MultipartFile multipartFile) {
        def folder = storage.createFolder(UUID.randomUUID().toString())
        uploadAsync(multipartFile, folder.getId()).get()
        new Image(filename: multipartFile.originalFilename, folderId: folder.getId())
    }

    def delete(String folder) {
        storage.deleteFolder(folder)
    }

    private uploadAsync(MultipartFile multipartFile, String parentFolder) {
        String fileName = multipartFile.originalFilename
        String contentType = multipartFile.contentType
        String ext = FilenameUtils.getExtension(fileName)
        def thumbNames = thumbnailNames(fileName)
        File original = File.createTempFile("img", ".$ext")
        multipartFile.transferTo(original)

        tasks ({
            storage.uploadFile(thumbNames.l, contentType, new FileInputStream(original.absolutePath), parentFolder)
        }, {
            storage.uploadFile(thumbNames.m, contentType, createThumbnail(original, ext, 512), parentFolder)
        }, {
            storage.uploadFile(thumbNames.s, contentType, createThumbnail(original, ext, 256), parentFolder)
        })
    }

}
