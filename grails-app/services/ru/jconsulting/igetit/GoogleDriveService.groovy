package ru.jconsulting.igetit

import com.google.api.client.http.ByteArrayContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.ParentReference
import org.springframework.web.multipart.MultipartFile

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 11.05.14 18:51
 */
class GoogleDriveService {

    final static FOLDER_MIME_TYPE = "application/vnd.google-apps.folder"

    def grailsApplication

    Drive driveService

    File uploadFile(MultipartFile multipartFile, String parentFolder) {

        def info = new File()
                .setTitle(multipartFile.originalFilename)
                .setDescription(multipartFile.originalFilename)
                .setMimeType(multipartFile.contentType)
                .setParents([new ParentReference().setId(parentFolder)])

        def content = new ByteArrayContent(multipartFile.contentType, multipartFile.bytes)

        driveService.files().insert(info, content).execute()
    }

    File createFolder(String name) {
        createFolder(name, grailsApplication.config.google.drive.rootFolderId as String)
    }

    File createFolder(String name, String parentFolder) {

        def info = new File()
                .setTitle(name)
                .setMimeType(FOLDER_MIME_TYPE)
                .setParents([new ParentReference().setId(parentFolder)])

        driveService.files().insert(info).execute()
    }
}
