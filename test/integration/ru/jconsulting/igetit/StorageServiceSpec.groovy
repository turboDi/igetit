package ru.jconsulting.igetit

import com.google.common.io.Files
import org.apache.commons.io.FileUtils
import org.springframework.mock.web.MockMultipartFile
import ru.jconsulting.igetit.image.Thumbnail
import spock.lang.Specification

/**
 *
 *
 * @author Dmitriy Borisov
 * @created 18.10.14 19:59
 */
class StorageServiceSpec extends Specification {

    def storageService

    def setup() {
        storageService.storage.root = Files.createTempDir()
    }

    def cleanup() {
        FileUtils.deleteDirectory(new File(storageService.storage.root))
    }

    void "test upload" () {
        given:
        def file = new File('test/integration/resources/1024x1024.jpg')
        def t = [l: new Thumbnail(), m: new Thumbnail(size: 256, x: 100, y: 100, width: 500), s: new Thumbnail(size: 128, x: 100, y: 100, width: 500)]
        when:
        Image image = storageService.upload(new MockMultipartFile(file.name, file.name, 'image/jpeg', file.bytes), t)
        then:
        image.filename == file.name
        def original = new File(image.folderId + File.separator + 'l.jpg')
        original.exists() && original.bytes.size() > 0
        new File(image.folderId + File.separator + 'm.jpg').exists()
        new File(image.folderId + File.separator + 's.jpg').exists()
    }

    void "test delete" () {
        given:
        def file = new File('test/integration/resources/1024x1024.jpg')
        def t = [l: new Thumbnail(), m: new Thumbnail(size: 256, x: 100, y: 100, width: 500), s: new Thumbnail(size: 128, x: 100, y: 100, width: 500)]
        when:
        Image image = storageService.upload(new MockMultipartFile(file.name, file.name, 'image/jpeg', file.bytes), t)
        storageService.delete(image.folderId)
        then:
        !new File(image.folderId).exists()
    }

}
