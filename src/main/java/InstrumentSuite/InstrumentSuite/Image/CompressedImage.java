//package InstrumentSuite.InstrumentSuite.Image;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Entity
//public class CompressedImage {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Lob
//    private byte[] imageData;
//
//
//    @Column
//    private int width;
//
//    @Column
//    private int height;
//
//    @Column
//    private String format;
//
//    @Column(columnDefinition = "TEXT")
//    private String base64EncodedImage;
//
//}
