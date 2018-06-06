package com.fileserver.file;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author BelkinSergei
 */
public interface FileRepository extends JpaRepository<FileEntry, Long> {
    
}
