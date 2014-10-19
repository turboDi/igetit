package ru.jconsulting.igetit

class Image {

    String filename

    String folderId

    static constraints = {
        folderId unique: true
    }
}
