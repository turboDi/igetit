package ru.jconsulting.igetit

import org.apache.commons.io.FilenameUtils

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

import static org.imgscalr.Scalr.*

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

    static InputStream createThumbnail(File image, String ext, int targetSize) {
        File tmp = File.createTempFile("thumb", ".$ext")

        BufferedImage original = ImageIO.read(image)
        BufferedImage thumbnail = resize(original, Method.QUALITY, targetSize, OP_ANTIALIAS)
        if (ImageIO.write(thumbnail, ext, tmp)) {
            return new FileInputStream(tmp)
        }
        throw new IllegalStateException("Unable to create thumbnail")
    }
}
