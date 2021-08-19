package com.megait.nocoronazone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class VaccineService {

    private final String csvPath = "D:\\2021_15_webdev_hsa\\project_corona_before\\src\\main\\resources\\csv\\vaccine_article.csv";
    private final String batPath = "D:\\2021_15_webdev_hsa\\project_corona_before\\src\\main\\resources\\bat\\vaccine_article.bat";

}
