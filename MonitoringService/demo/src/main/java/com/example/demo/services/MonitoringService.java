package com.example.demo.services;


import com.example.demo.dtos.DeviceMeasurementDTO;
import com.example.demo.dtos.HourlyConsumptionDTO;
import com.example.demo.entities.DeviceInfo;
import com.example.demo.entities.DeviceMeasurement;
import com.example.demo.entities.HourlyEnergyConsumption;
import com.example.demo.repositories.DeviceInfoRepository;
import com.example.demo.repositories.DeviceMeasurementRepository;
import com.example.demo.repositories.HourlyEnergyConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MonitoringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringService.class);

    private final DeviceMeasurementRepository measurementRepository;
    private final HourlyEnergyConsumptionRepository hourlyConsumptionRepository;
    private final DeviceInfoRepository deviceInfoRepository;

    @Autowired
    public MonitoringService(DeviceMeasurementRepository measurementRepository,
                             HourlyEnergyConsumptionRepository hourlyConsumptionRepository,
                             DeviceInfoRepository deviceInfoRepository) {
        this.measurementRepository = measurementRepository;
        this.hourlyConsumptionRepository = hourlyConsumptionRepository;
        this.deviceInfoRepository = deviceInfoRepository;
    }

    @Transactional
    public void processMeasurement(DeviceMeasurementDTO dto) {
        // Save raw measurement
        DeviceMeasurement measurement = new DeviceMeasurement(
                dto.getDeviceId(),
                dto.getTimestamp(),
                dto.getMeasurementValue()
        );
        measurementRepository.save(measurement);

        // Get device info for max consumption threshold
        Optional<DeviceInfo> deviceInfoOpt = deviceInfoRepository.findById(dto.getDeviceId());
        if (deviceInfoOpt.isEmpty()) {
            LOGGER.warn("⚠️ Device info not found for device: {}", dto.getDeviceId());
            return;
        }

        DeviceInfo deviceInfo = deviceInfoOpt.get();

        // Calculate hourly consumption
        LocalDateTime hourTimestamp = dto.getTimestamp().truncatedTo(ChronoUnit.HOURS);
        updateHourlyConsumption(dto.getDeviceId(), hourTimestamp,
                dto.getMeasurementValue(), deviceInfo.getMaxConsumption());
    }

    @Transactional
    public void updateHourlyConsumption(UUID deviceId, LocalDateTime hourTimestamp,
                                        double measurementValue, double maxConsumption) {
        Optional<HourlyEnergyConsumption> existingOpt =
                hourlyConsumptionRepository.findByDeviceIdAndHourTimestamp(deviceId, hourTimestamp);

        HourlyEnergyConsumption hourly;

        if (existingOpt.isPresent()) {
            // Update existing record
            hourly = existingOpt.get();
            hourly.setTotalConsumption(hourly.getTotalConsumption() + measurementValue);
            hourly.setExceeded(hourly.getTotalConsumption() > maxConsumption);

            LOGGER.info("📈 Updated hourly consumption for device {} at {}: total={}",
                    deviceId, hourTimestamp, hourly.getTotalConsumption());
        } else {
            // Create new record
            hourly = new HourlyEnergyConsumption(deviceId, hourTimestamp,
                    measurementValue, maxConsumption);
            LOGGER.info("✨ Created new hourly consumption for device {} at {}: total={}",
                    deviceId, hourTimestamp, hourly.getTotalConsumption());
        }
