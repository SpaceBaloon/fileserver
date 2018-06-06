package com.fileserver.storage;

import java.io.File;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author BelkinSergei
 */
public interface Storage {
    
    String putFile(MultipartFile file, File zip ) throws IOException;
    public File getFile( File zip, String fileName ) throws IOException;
    
}
