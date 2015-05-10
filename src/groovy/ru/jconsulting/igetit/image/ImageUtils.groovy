package ru.jconsulting.igetit.image

import org.apache.commons.io.FilenameUtils
import org.imgscalr.Scalr

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import static org.imgscalr.Scalr.Method
import static org.imgscalr.Scalr.OP_ANTIALIAS

/**
 *
 *
 * @author Дмитрий Борисов
 * @created 12.10.14 19:47
 */
class ImageUtils {

    static Map thumbnailNames(String filename) {
        String ext = FilenameUtils.getExtension(filename)
        [l: "l.$ext", m: "m.$ext", s: "s.$ext"]
    }

    static Map thumbnailUrls(String folder, String filename) {
        thumbnailNames(filename).collectEntries { k, v -> [k, folder + v]}
    }

    static InputStream createThumbnail(File imageFile, String ext, Thumbnail thumbnail) {
        File tmp = File.createTempFile("thumb", ".$ext")
        thumbnail.image = ImageIO.read(imageFile)

        if (ImageIO.write(thumbnail.crop().resize().image, ext, tmp)) {
            return new FileInputStream(tmp)
        }
        throw new IllegalStateException("Unable to create thumbnail")
    }
}
