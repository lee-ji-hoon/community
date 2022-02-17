package com.community.profile.repository;

import com.community.profile.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

    Zone findByCityAndProvince(String cityName, String provinceName);
}
