package ru.jconsulting.igetit.storage

import ru.jconsulting.igetit.Image

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 13.10.14 22:28
 */
public interface Storage {

    def uploadFile(def fileName, String contentType, InputStream is, String parentFolder)

    def createFolder(String name)

    def deleteFolder(String folderId)

    Map getURL(Image image)
}