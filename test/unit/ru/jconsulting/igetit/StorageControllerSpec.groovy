package ru.jconsulting.igetit

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.access.AccessDeniedException
import spock.lang.Specification

@TestFor(StorageController)
@Mock(Image)
class StorageControllerSpec extends Specification {

    def setup() {
        new Image(folderId: 'existing', filename: '123').save(flush: true, failOnError: true)
        def storageServiceMock = mockFor(StorageService)
        storageServiceMock.demand.upload(0..1) { def file, map ->
            new Image(filename: file.originalFilename, folderId: '1')
        }
        storageServiceMock.demand.delete(0..1) { -> }
        controller.storageService = storageServiceMock.createMock()
    }

    void "test undefined upload"() {
        when:
        controller.upload()
        then:
        response.status == 422
    }

    void "test upload"() {
        given:
        params.file = new MockMultipartFile('name', 'originalFilename', 'type', 'content'.bytes)
        when:
        controller.upload()
        then:
        response.status == 200
        response.json.filename == 'originalFilename'
        response.json.folderId == '1'
    }

    void "test undefined delete"() {
        when:
        controller.delete()
        then:
        response.status == 422
    }

    void "test delete"() {
        given:
        params.folderId = '1'
        when:
        controller.delete()
        then:
        response.status == 204
    }

    void "test referenced delete"() {
        given:
        params.folderId = 'existing'
        when:
        controller.delete()
        then:
        thrown(AccessDeniedException)
    }
}
