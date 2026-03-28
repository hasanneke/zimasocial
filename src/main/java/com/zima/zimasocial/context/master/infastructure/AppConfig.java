package com.zima.zimasocial.context.master.infastructure;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "APP_CONFIG")
@Getter
public class AppConfig {
    @Id
    @Column(name = "code")
    private String id;
    @Column(name = "ads_enabled")
    private Boolean adsEnabled;
}
