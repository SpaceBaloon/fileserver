package com.fileserver.storage;

import java.io.File;

/**
 *
 * @author BelkinSergei
 */
public class TempFile {
    
    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
    private String realName;

    public TempFile(File file, String realName) {
        this.file = file;
        this.realName = realName;
    }
    
    
}
