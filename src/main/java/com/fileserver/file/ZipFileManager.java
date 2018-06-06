package com.fileserver.file;

import com.fileserver.storage.Storage;
import com.fileserver.storage.TempFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author BelkinSergei
 */
@Service
public class ZipFileManager implements FileManager {
    
    private final FileRepository repository;
    private final Storage storage;

    @Autowired
    public ZipFileManager( FileRepository repository, Storage storage ) {
        this.repository = repository;
        this.storage = storage;
    }

    @Override
    public List<FileEntry> listAllFiles() {
        return repository.findAll();
    }

    @Override
    public TempFile getFile( File zip, Long id ) throws FileNotFoundException {
        File result = null;
        FileEntry entry;
        try {
            entry = repository.findById(id).orElseThrow( () -> {
                return new RuntimeException( "File with id: " + id + " not found in the database." );
            });
            result = storage.getFile( zip, entry.getFileName() );
        } catch ( IOException ex ) {
            resolveFileExistence( id );
            throw new FileNotFoundException( ex.getMessage() );
        }
        return new TempFile( result, entry.getFileName() );
    }
    
    @Async
    public void resolveFileExistence( Long id ) {
        FileEntry entry = repository.findById(id).orElse( null );
        if( entry != null ) {
            entry.setExist(Boolean.FALSE);
            repository.save( entry );
        }
    }
    
    @Override
    public void saveFile(MultipartFile file, File zip) {
        try {
            String fileName = storage.putFile( file, zip );
            
            FileEntry entry = new FileEntry();
            entry.setFileName( fileName );
            entry.setExist( Boolean.TRUE );
            repository.save( entry );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
        
    }
    
}
