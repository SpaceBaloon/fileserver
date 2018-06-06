package com.fileserver.file;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 *
 * @author BelkinSergei
 */
@Entity
public class FileEntry implements Serializable {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @NotNull
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    @NotNull
    private Boolean exist;

    public Boolean isExists() {
        return exist;
    }

    public void setExist(Boolean exist) {
        this.exist = exist;
    }

    @Override
    public int hashCode() {
        return super.hashCode(); 
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString(); 
    }
    
}
