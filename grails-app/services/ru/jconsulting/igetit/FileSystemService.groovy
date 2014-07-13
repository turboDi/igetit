package ru.jconsulting.igetit

import org.springframework.web.multipart.MultipartFile

class FileSystemService {

    String root

    class FSFile {
        String id
        String originalFilename
    }

    def uploadFile(MultipartFile multipartFile, String parentFolder) {
        def f = new File(parentFolder + File.separator + multipartFile.originalFilename)
        multipartFile.transferTo(f)
        new FSFile(id: f.absolutePath, originalFilename: f.name)
    }

    def createFolder(String name) {
        createFolder(name, root)
    }

    def createFolder(String name, String parentFolder) {
        def f = new File(parentFolder + File.separator + name)
        if (!f.mkdirs()) {
            throw new RuntimeException("folder creation failed")
        }
        new FSFile(id: f.absolutePath, originalFilename: f.name)
    }

    String getURL(Image image) {
        image.folderId + File.separator + image.filename
    }
}
