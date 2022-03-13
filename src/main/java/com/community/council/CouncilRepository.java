package com.community.council;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouncilRepository extends JpaRepository<Council, Long> {

    List<Council> findAllByPostSortOrderByEventEndDateDesc(String postSort);

    List<Council> findTop5ByPostSortOrderByUploadTimeDesc(String postSort);


    Council findByCid(long cid);

}
