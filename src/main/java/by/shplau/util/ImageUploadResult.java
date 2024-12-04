package by.shplau.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageUploadResult {
    private String imageFileName;
    private String thumbnailFileName;
}
