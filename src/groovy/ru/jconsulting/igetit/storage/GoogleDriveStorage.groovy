package ru.jconsulting.igetit.storage

import com.google.api.client.http.InputStreamContent
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import com.google.api.services.drive.model.ParentReference
import ru.jconsulting.igetit.Image
import ru.jconsulting.igetit.image.ImageUtils

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 11.05.14 18:51
 */
class GoogleDriveStorage implements Storage {

    final static FOLDER_MIME_TYPE = "application/vnd.google-apps.folder"

    final static HOST_URL = "https://googledrive.com/host/"

    Drive driveService

    File uploadFile(def fileName, String contentType, InputStream is, String parentFolder) {

        def info = new File()
                .setTitle(fileName)
                .setDescription(fileName)
                .setMimeType(contentType)
                .setParents([new ParentReference().setId(parentFolder)])

        def content = new InputStreamContent(contentType, is)

        driveService.files().insert(info, content).execute()
    }

    File createFolder(String name) {
        createFolder(name, System.getenv("DRIVE_ROOT_ID"))
    }

    def deleteFolder(String folderId) {
        driveService.files().delete(folderId).execute()
    }

    File createFolder(String name, String parentFolder) {

        def info = new File()
                .setTitle(name)
                .setMimeType(FOLDER_MIME_TYPE)
                .setParents([new ParentReference().setId(parentFolder)])

        driveService.files().insert(info).execute()
    }

    Map getURL(Image image) {
        ImageUtils.thumbnailUrls(HOST_URL + image.folderId + "/", image.filename)
    }
}
