package com.community.domain.council;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouncilRepository extends JpaRepository<Council, Long> {

    List<Council> findByPostTitleContainingOrderByUploadTimeDesc(String keyword);
    List<Council> findByPostContentContainingOrderByUploadTimeDesc(String keyword);

    List<Council> findAllByPostSortOrderByEventEndDateDesc(String postSort);

    List<Council> findTop4ByPostSortOrderByUploadTimeDesc(String postSort);

    Page<Council> findByPostSortOrderByUploadTimeDesc(String postSort, Pageable pageable);

    Page<Council> findByPostSortOrPostTitleContainingOrPostContentContainingOrderByUploadTimeDesc(String type, String keyword1, String keyword2, Pageable pageable);


    Council findByCid(long cid);

}
