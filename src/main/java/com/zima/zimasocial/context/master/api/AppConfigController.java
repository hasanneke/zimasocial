package com.zima.zimasocial.context.master.api;

import com.zima.zimasocial.context.master.infastructure.AppConfig;
import com.zima.zimasocial.context.master.infastructure.AppConfigJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/app-config")
@RequiredArgsConstructor
public class AppConfigController {
    private final AppConfigJpaRepository appConfigJpaRepository;
    @GetMapping
    public ResponseEntity<AppConfig> get() {
        return ResponseEntity.ok(appConfigJpaRepository.findById("prod").orElse(null));
    }
}
