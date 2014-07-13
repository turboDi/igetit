package ru.jconsulting.igetit

import com.google.common.io.Files
import grails.test.mixin.TestFor
import org.apache.commons.io.FileUtils
import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile
import spock.lang.Specification

@TestFor(FileSystemService)
class FileSystemServiceSpec extends Specification {

    File storageRoot

    def setup() {
        storageRoot = Files.createTempDir()
        service.root = storageRoot.absolutePath
    }

    def cleanup() {
        FileUtils.deleteDirectory(storageRoot)
    }

    void "test create folder"() {
        given:
        def expectedLocation = service.root + File.separator + 'test'
        when:
        def folder = service.createFolder("test")
        then:
        folder.id == expectedLocation
        folder.originalFilename == 'test'
        new File(expectedLocation).isDirectory()
    }

    void "test upload file"() {
        given:
        def expectedLocation = service.root + File.separator + 'folderWithFile' + File.separator + 'testFile.txt'
        def file = new GrailsMockMultipartFile('testFile', 'testFile.txt', null, 'test content'.bytes)
        def folder = service.createFolder('folderWithFile')
        when:
        def uploaded = service.uploadFile(file, folder.id)
        then:
        uploaded.id == expectedLocation
        uploaded.originalFilename == 'testFile.txt'
        file.targetFileLocation.path == expectedLocation
    }

    void "test get url"() {
        given:
        def image = new Image(filename: 'test.gif', folderId: '~')
        when:
        def url = service.getURL(image)
        then:
        url == '~' + File.separator + 'test.gif'
    }
}
