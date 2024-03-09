//package InstrumentSuite.InstrumentSuite.Image;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.util.Base64;
//import net.coobird.thumbnailator.Thumbnails;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CompressionService {
//
//    private final CompressedImageRepository compressedImageRepository;
//    private final Path rootLocation;
//
//    @Autowired
//    public CompressionService(CompressedImageRepository compressedImageRepository, Path rootLocation) {
//        this.compressedImageRepository = compressedImageRepository;
//        this.rootLocation = rootLocation;
//    }
//
//    public CompressedImage compressImage(byte[] imageData, String originalFileName) throws IOException {
//        System.out.println("ROOT LOCATION: " + rootLocation);
//
//        // Check if the image data is empty
//        if (imageData.length == 0) {
//            throw new IOException("Cannot compress an empty image.");
//        }
//
//        byte[] compressedImageBytes;
//        int compressedWidth = 300;
//        int compressedHeight = 300;
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        try {
//            Thumbnails.of(new ByteArrayInputStream(imageData))
//                    .size(compressedWidth, compressedHeight)
//                    .toOutputStream(outputStream);
//            compressedImageBytes = outputStream.toByteArray();
//        } catch (IOException e) {
//            throw e;
//        } finally {
//            outputStream.close();
//        }
//
//        // Encode the compressed bytes to Base64
//        String base64EncodedImage = Base64.getEncoder().encodeToString(compressedImageBytes);
//
//        // Save the compressed image to the file system
//        String compressedFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.')) + "_compressed.jpg";
//        Path compressedImagePath = rootLocation.resolve(compressedFileName);
//        try (FileOutputStream fileOutputStream = new FileOutputStream(compressedImagePath.toFile())) {
//            fileOutputStream.write(compressedImageBytes);
//        }
//
//        // Create a CompressedImage object and save it to the database
//        CompressedImage compressedImage = new CompressedImage();
//        compressedImage.setImageData(base64EncodedImage.getBytes()); // Assuming imageData is a byte array in CompressedImage
//        compressedImage.setBase64EncodedImage(base64EncodedImage); // Assuming there is a field to store Base64 string in CompressedImage
//        compressedImage.setWidth(compressedWidth);
//        compressedImage.setHeight(compressedHeight);
//        compressedImage.setFormat("JPEG");
//
//        return compressedImageRepository.save(compressedImage);
//    }
//}