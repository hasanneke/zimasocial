package com.zima.zimasocial.repository;

import com.zima.zimasocial.entity.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeviceTokeJpaRepository extends JpaRepository<UserDeviceToken, Long> {
}
