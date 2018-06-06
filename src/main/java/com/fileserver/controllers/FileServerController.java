package com.fileserver.controllers;

import com.fileserver.file.FileEntry;
import com.fileserver.file.FileManager;
import com.fileserver.storage.TempFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author BelkinSergei
 */
@Controller
public class FileServerController {
    
    private final String uploadsFolder;
    private final FileManager fileManager;
    
    @Autowired
    public FileServerController( FileManager fileManager, @Value("${zip.path}") String zipFilePath ) {
        this.fileManager = fileManager;  
        this.uploadsFolder = zipFilePath;
    }
    
    @GetMapping( "/" )
    public String listAllAvailableFiles( Model model ) {
        List<FileEntry> listAllFiles = fileManager.listAllFiles();
        model.addAttribute( "files", listAllFiles );
        return "fileListPage";
    }
    
    @GetMapping( "/{id}" )
    @ResponseBody
    public ResponseEntity<?> downloadFile( @PathVariable Long id, 
            HttpServletRequest req, HttpServletResponse resp ) throws FileNotFoundException, IOException {
        TempFile file = fileManager.getFile( 
                new File( req.getServletContext().getRealPath( uploadsFolder ) ),  
                id 
        );
        
        Resource result = new UrlResource( file.getFile().toURI() );
        return ResponseEntity.ok()
                .header( HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" 
                        + file.getRealName()
                        + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body( result );
        
    }
    
    @PostMapping( "/" )
    public String uploadFile( 
            @RequestParam( "file" ) MultipartFile file, 
            RedirectAttributes redirectAttr, HttpServletRequest req ) {
        fileManager.saveFile( file, new File( req.getServletContext().getRealPath( uploadsFolder ) ) );
        redirectAttr.addFlashAttribute( "message", "Your file was successfully uploaded!" );
        return "redirect:/";
        
    }
    
    @ExceptionHandler( FileNotFoundException.class )
    public ModelAndView handleExceptions( HttpServletRequest request, Exception ex ) {
        ex.printStackTrace( System.err );
        ModelAndView model = new ModelAndView( "errorPage" );
        model.addObject( "url", request.getRequestURI() );
        model.addObject( "exception", ex );
        return model;
    }
    
    
}
