package ru.jconsulting.igetit

class Image {

    String filename

    String fileId

    String folderId

    static belongsTo = Buy

    static constraints = {
    }
}
