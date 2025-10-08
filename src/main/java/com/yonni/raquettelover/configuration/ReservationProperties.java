package com.yonni.raquettelover.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

import java.time.LocalTime;

@Component
@ConfigurationProperties(prefix = "reservation")
@Data
public class ReservationProperties {

    private LocalTime openingTime;
    private LocalTime closingTime;
    private int slotDuration;
}
