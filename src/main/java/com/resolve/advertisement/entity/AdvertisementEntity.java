package com.resolve.advertisement.entity;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
@Entity
@Table(name="advertisement")
public class AdvertisementEntity extends  AuditEntiy{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addId;

    @NotNull
    @Digits(integer = 6, fraction = 7, message = "at max 7 precision allowed")
    private Double latitude;
    @NotNull
    @Digits(integer = 6, fraction = 7, message = "at max 7 precision allowed")
    private Double longitude;
    @NotBlank
    @URL(message = "href is invalid")
    private String href;
    @NotBlank
    private String advertisementName;
}
