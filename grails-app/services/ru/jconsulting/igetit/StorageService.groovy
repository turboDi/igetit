package ru.jconsulting.igetit

import org.apache.commons.io.FilenameUtils
import org.springframework.web.multipart.MultipartFile
import ru.jconsulting.igetit.image.Thumbnail

import static grails.async.Promises.tasks
import static ru.jconsulting.igetit.image.ImageUtils.*

class StorageService {

    def storage

    def upload(MultipartFile multipartFile, Map sizes) {
        def folder = storage.createFolder(UUID.randomUUID().toString())
        uploadAsync(multipartFile, folder.getId(), sizes).get()
        new Image(filename: multipartFile.originalFilename, folderId: folder.getId())
    }

    def delete(String folder) {
        storage.deleteFolder(folder)
    }

    private uploadAsync(MultipartFile multipartFile, String parentFolder, Map<String, Thumbnail> thumbnails) {
        String fileName = multipartFile.originalFilename
        String ext = FilenameUtils.getExtension(fileName)
        File original = File.createTempFile("img", ".$ext")
        multipartFile.transferTo(original)

        tasks( thumbnails.collect { thumbnail -> return {
                storage.uploadFile(thumbnailNames(fileName).get(thumbnail.key), multipartFile.contentType,
                        createThumbnail(original, ext, thumbnail.value), parentFolder)
        }} )

    }

}
