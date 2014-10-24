package ru.jconsulting.igetit.storage

import org.apache.commons.io.FileUtils
import ru.jconsulting.igetit.Image
import ru.jconsulting.igetit.ImageUtils

class FileSystemStorage implements Storage {

    String root

    class FSFile {
        String id
        String originalFilename
    }

    def uploadFile(def fileName, String contentType, InputStream is, String parentFolder) {
        def f = new File(parentFolder + File.separator + fileName)
        f.withOutputStream {
            it << is
            is.close()
        }
        new FSFile(id: f.absolutePath, originalFilename: f.name)
    }

    def createFolder(String name) {
        createFolder(name, root)
    }

    def deleteFolder(String path) {
        FileUtils.deleteDirectory(new File(path))
    }

    def createFolder(String name, String parentFolder) {
        def f = new File(parentFolder + File.separator + name)
        if (!f.mkdirs()) {
            throw new RuntimeException("folder creation failed")
        }
        new FSFile(id: f.absolutePath, originalFilename: f.name)
    }

    Map getURL(Image image) {
        ImageUtils.thumbnailUrls(image.folderId + File.separator, image.filename)
    }
}
