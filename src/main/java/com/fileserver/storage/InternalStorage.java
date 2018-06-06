package com.fileserver.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.IIOException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author BelkinSergei
 */
@Service
public class InternalStorage implements Storage {
        
private boolean REJECT_DUPLICATES = false;
    
    /**
     * Not thread safe.
     * 
     * @param file - file to be added to storage
     * @param storage - is considered as zip file
     * @return - name of file that was assinged
     * @throws IOException 
     */
    @Override
    public String putFile( MultipartFile file, File storage ) throws IOException {        
        //create if not exists
        boolean wasCreated = storage.createNewFile();
        //retrieve real file name
        String fileName = StringUtils.cleanPath( file.getOriginalFilename() );
        fileName = StringUtils.getFilename( fileName ); 
        //we need old path to create new file further
        final String newStoragePath = storage.getPath();
        //create temp zip file
        File tempZip = File.createTempFile( newStoragePath, "" );
        //we need only reference to it
        tempZip.delete();
        //rename(move) current storage file
        if( !( storage.renameTo( tempZip ) ) ) {
            throw new IIOException( "Can't rename file." );
        }
        //copy file
        try (
                ZipInputStream zin = new ZipInputStream( new FileInputStream( tempZip ) );
                ZipOutputStream zout = new ZipOutputStream( new FileOutputStream( newStoragePath ) );
                InputStream fis = file.getInputStream();
                ) {
            
            Boolean matching = false;
            byte[] buffer = new byte[ 1024 * 8 ];
            int length = 0;
            ZipEntry entry;
            
            while( (entry = zin.getNextEntry() ) != null ) {
                if( !matching )
                    matching = entry.getName().equals( fileName );
                zout.putNextEntry( entry );
                while( (length = zin.read( buffer ) ) > 0 ) {
                    zout.write( buffer, 0, length );
                }
                zout.closeEntry();
            }   
            
            //append new file
            if( matching && !REJECT_DUPLICATES ) 
                fileName = resolveMatching( fileName );
            zout.putNextEntry( new ZipEntry( fileName ) );            
            while( ( length = fis.read( buffer ) ) > 0 ) {
                zout.write( buffer, 0, length );
            }
            zout.closeEntry();
            
        }
        //finally delete temp file
        tempZip.delete();
        return fileName;
    }
    
    private String resolveMatching( String oldName ) {
        return UUID.randomUUID().toString() + oldName;
    }
    
    /**
     * It's needed to manage returning files.
     * 
     * @param storage - zip storage
     * @param fileName - file name without path info
     * @return - temprorally created file
     * @throws IOException 
     */
    @Override
    public File getFile( File storage, String fileName ) throws IOException {
        //create temp file
        File file = File.createTempFile( fileName, "" );
        try(
                ZipInputStream zin = new ZipInputStream( new FileInputStream( storage ) );
                OutputStream fout = new FileOutputStream( file );
                ){
            ZipEntry entry;
            byte[] buffer = new byte[ 1024 * 8 ];
            int length = 0;
            //try to find our file
            while( ( entry = zin.getNextEntry() ) != null && !entry.getName().equals( fileName ) ) {}
            //copy file or throw exception
            if( entry != null ) {
                while( ( length = zin.read( buffer ) ) > 0 ) {
                    fout.write( buffer, 0, length );
                }
            } else {
                throw new FileNotFoundException( "File " + fileName +  " was not found." );
            }                
        }
        return file;
    }
    
}
