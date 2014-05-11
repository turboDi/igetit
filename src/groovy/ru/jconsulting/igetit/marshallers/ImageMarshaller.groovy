package ru.jconsulting.igetit.marshallers

import grails.converters.JSON
import ru.jconsulting.igetit.Image

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 05.05.14 21:46
 */
class ImageMarshaller implements MarshallerRegistrar {

    final static HOST_URL = "https://googledrive.com/host/"

    @Override
    void register() {
        JSON.registerObjectMarshaller(Image){ Image image ->
            return [
                    filename: image.filename,
                    fileId: image.fileId,
                    folderId: image.folderId,
                    url: getFileURL(image)
            ]
        }
    }

    //TODO move to google drive service
    String getFileURL(Image image) {
        HOST_URL + "$image.folderId/$image.filename"
    }
}
