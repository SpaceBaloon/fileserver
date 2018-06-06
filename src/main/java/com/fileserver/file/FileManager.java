package com.fileserver.file;

import com.fileserver.storage.TempFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author BelkinSergei
 */
public interface FileManager {
    
    public void saveFile(MultipartFile file, File zip);
    List<FileEntry> listAllFiles();
    TempFile getFile( File zip, Long id ) throws FileNotFoundException;
    
}
