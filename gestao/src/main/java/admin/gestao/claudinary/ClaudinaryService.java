package admin.gestao.claudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ClaudinaryService {

    @Autowired
        private  Cloudinary cloudinary;

        public ClaudinaryService() {
            this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", System.getenv("CLOUDINARY_CLOUD_NAME"),
                    "api_key", System.getenv("CLOUDINARY_API_KEY"),
                    "api_secret", System.getenv("CLOUDINARY_API_SECRET")
            ));
        }

        public String uploadImage(byte[] imageBytes, String publicId) throws IOException, IOException {
            return cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                    "public_id", publicId,
                    "overwrite", true
            )).get("secure_url").toString();
        }

        public void deleteImage(String publicId) throws IOException {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }


}
