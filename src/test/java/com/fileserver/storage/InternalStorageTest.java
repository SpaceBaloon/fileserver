package com.fileserver.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author BelkinSergei
 */
public class InternalStorageTest {
    
    public InternalStorageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    final String fileName = "f:/Word.doc";
    
    final String cleanFileName = "Word.doc";
    final String storageName = "f:/uploads.zip";
    @Test
    public void testAddition() throws IOException {
        
        System.out.println( "Test addition." );
        Storage basicStorage = new InternalStorage();
        File file = new File( storageName );
        
        String name = fileName;
            try ( InputStream is = new FileInputStream( name )) {
                MultipartFile uploadFile = new MockMultipartFile( name, 
                        name, 
                        "multipart/form-data", 
                        is
                );
                basicStorage.putFile( uploadFile, file );
            }   
    }
    
    @Test
    public void testReceive() throws IOException {
        
        System.out.println( "Test receive." );
        
        Storage basicStorage = new InternalStorage();
        File storage = new File( storageName );
        File result = basicStorage.getFile( storage, cleanFileName );
        System.out.println( "File was successfully obtained " + result.getPath() );
        
    }
}
