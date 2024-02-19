package com.puj.domain.attachfile.repository;

import com.puj.domain.attachfile.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;

// Spring Data JPA 사용
public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {
}
