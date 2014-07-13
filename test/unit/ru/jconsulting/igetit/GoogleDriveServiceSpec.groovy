package ru.jconsulting.igetit

import com.google.api.client.http.AbstractInputStreamContent
import com.google.api.client.testing.http.MockHttpTransport
import com.google.api.client.testing.json.MockJsonFactory
import com.google.api.services.drive.Drive
import com.google.api.services.drive.model.File
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.plugins.testing.GrailsMockMultipartFile
import spock.lang.Specification

@TestFor(GoogleDriveService)
class GoogleDriveServiceSpec extends Specification {

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
        service.driveService = driveMock
    }

    void "test create folder"() {
        when:
        def file = service.createFolder('folder', 'parent')
        then:
        file.getId() == '1'
        file.getOriginalFilename() == 'folder'
        file.getMimeType() == service.FOLDER_MIME_TYPE
        file.getParents()[0].getId() == 'parent'
    }

    void "test create file"() {
        when:
        def file = service.uploadFile(new GrailsMockMultipartFile('testFile', 'testFile.txt', 'type', 'content'.bytes), 'parent')
        then:
        file.getId() == '1'
        file.getOriginalFilename() == 'testFile.txt'
        file.getMimeType() == 'type'
        file.getParents()[0].getId() == 'parent'
    }

    void "test get url"() {
        given:
        def image = new Image(filename: 'test.gif', folderId: '123')
        when:
        def url = service.getURL(image)
        then:
        url == 'https://googledrive.com/host/123/test.gif'
    }
}
