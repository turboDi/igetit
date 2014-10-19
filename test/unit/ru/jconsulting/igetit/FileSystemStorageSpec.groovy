package ru.jconsulting.igetit

import com.google.common.io.Files
import org.apache.commons.io.FileUtils
import ru.jconsulting.igetit.storage.FileSystemStorage
import spock.lang.Specification

class FileSystemStorageSpec extends Specification {

    FileSystemStorage tested

    def setup() {
        tested = new FileSystemStorage(root: Files.createTempDir())
    }

    def cleanup() {
        FileUtils.deleteDirectory(new File(tested.root))
    }

    void "test create folder"() {
        given:
        def expectedLocation = tested.root + File.separator + 'test'
        when:
        def folder = tested.createFolder("test")
        then:
        folder.id == expectedLocation
        folder.originalFilename == 'test'
        new File(expectedLocation).isDirectory()
    }

    void "test upload file"() {
        given:
        def expectedLocation = tested.root + File.separator + 'folderWithFile' + File.separator + 'testFile.txt'
        def folder = tested.createFolder('folderWithFile')
        when:
        def uploaded = tested.uploadFile('testFile.txt', '', new ByteArrayInputStream('123'.bytes), folder.id)
        then:
        uploaded.id == expectedLocation
        uploaded.originalFilename == 'testFile.txt'
        new File(expectedLocation).text == '123'
    }

    void "test delete file"() {
        given:
        def folder = new File(tested.root + File.separator + 'toDelete')
        assert folder.mkdir()
        when:
        tested.deleteFolder(folder.absolutePath)
        then:
        !folder.exists()
    }

    void "test get url"() {
        given:
        def image = new Image(filename: 'test.gif', folderId: '~')
        when:
        def url = tested.getURL(image)
        then:
        url.l == '~' + File.separator + 'l.gif'
        url.m == '~' + File.separator + 'm.gif'
        url.s == '~' + File.separator + 's.gif'
    }
}
