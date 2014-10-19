package ru.jconsulting.igetit

import com.google.api.client.http.AbstractInputStreamContent
import com.google.api.client.testing.http.MockHttpTransport
import com.google.api.client.testing.json.MockJsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import ru.jconsulting.igetit.storage.GoogleDriveStorage
import spock.lang.Specification

@TestMixin(GrailsUnitTestMixin)
class GoogleDriveStorageSpec extends Specification {

    GoogleDriveStorage tested

    def setup() {
        def driveMock = new Drive.Builder(new MockHttpTransport(), new MockJsonFactory(), null)
                .setApplicationName("test")
                .build()
        def driveFilesMock = mockFor(Drive.Files)
        driveFilesMock.demand.insert { File info ->
            [execute : {info.setOriginalFilename(info.getTitle()).setId('1')}]
        }
        driveFilesMock.demand.insert { File info, AbstractInputStreamContent content ->
            [execute : {info.setOriginalFilename(info.getTitle()).setId('1')}]
        }
        def driveFiles = driveFilesMock.createMock()
        driveMock.metaClass.files {driveFiles}
        tested = new GoogleDriveStorage(driveService: driveMock)
    }

    void "test create folder"() {
        when:
        def file = tested.createFolder('folder', 'parent')
        then:
        file.getId() == '1'
        file.getOriginalFilename() == 'folder'
        file.getMimeType() == tested.FOLDER_MIME_TYPE
        file.getParents()*.getId() == ['parent']
    }

    void "test create file"() {
        when:
        def file = tested.uploadFile('testFile.txt', 'type', new ByteArrayInputStream('content'.bytes), 'parent')
        then:
        file.getId() == '1'
        file.getOriginalFilename() == 'testFile.txt'
        file.getMimeType() == 'type'
        file.getParents()*.getId() == ['parent']
    }

    void "test get url"() {
        given:
        def image = new Image(filename: 'test.gif', folderId: '123')
        when:
        def url = tested.getURL(image)
        then:
        url.l == 'https://googledrive.com/host/123/l.gif'
        url.m == 'https://googledrive.com/host/123/m.gif'
        url.s == 'https://googledrive.com/host/123/s.gif'
    }
}
