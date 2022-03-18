package com.community.council;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouncilRepository extends JpaRepository<Council, Long> {

    List<Council> findByPostTitleContainingOrderByUploadTimeDesc(String keyword);
    List<Council> findByPostContentContainingOrderByUploadTimeDesc(String keyword);

    List<Council> findAllByPostSortOrderByEventEndDateDesc(String postSort);

    List<Council> findTop4ByPostSortOrderByUploadTimeDesc(String postSort);


    Council findByCid(long cid);

}
