package com.community.infra.aws;

import org.springframework.data.jpa.repository.JpaRepository;

public interface S3Repository extends JpaRepository<S3, Long> {

    S3 findByImageName(String imageName);
}
