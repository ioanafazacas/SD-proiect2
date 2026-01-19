package com.example.demo.controllers;

import com.example.demo.dtos.ConsumptionStatsDTO;
import com.example.demo.dtos.DeviceMeasurementDTO;
import com.example.demo.dtos.HourlyConsumptionDTO;
import com.example.demo.services.LoadBalancingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/load_balancer")
@Validated
@Tag(name = "Monitoring API", description = "Monitor device energy consumption and analyze data")
public class LoadBalancerController {

    private final LoadBalancingService loadBalancingService;

    @Autowired
    public LoadBalancerController(LoadBalancingService loadBalancingService) {
        this.loadBalancingService = loadBalancingService;
    }

    @Operation(summary = "Get hourly consumption for a device for one day")
    @GetMapping("/consumption/daily/{deviceId}")
    public ResponseEntity<List<HourlyConsumptionDTO>> getDailyDeviceConsumption(
            @PathVariable UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<HourlyConsumptionDTO> result =
                loadBalancingService.getHourlyConsumptionInRange(deviceId, start, end);

        return ResponseEntity.ok(result);
    }


    @Operation(
            summary = "Get hourly consumption for multiple devices",
            description = "Retrieves hourly consumption for all user's devices on a specific date"
    )
    @GetMapping("/consumption/daily/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<HourlyConsumptionDTO>> getUserDailyConsumption(
            @Parameter(description = "User ID")
            @PathVariable UUID userId,

            @Parameter(description = "Date in format yyyy-MM-dd", example = "2024-01-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

        List<HourlyConsumptionDTO> consumption =
                loadBalancingService.getUserHourlyConsumptionInRange(userId, startOfDay, endOfDay);

        return ResponseEntity.ok(consumption);
    }

    @Operation(summary = "Get all measurements for a device")
    @GetMapping("/device/{deviceId}/measurements")
    public ResponseEntity<List<DeviceMeasurementDTO>> getDeviceMeasurements(@PathVariable UUID deviceId) {
        List<DeviceMeasurementDTO> measurements = loadBalancingService.getDeviceMeasurements(deviceId);
        return ResponseEntity.ok(measurements);
    }

    @Operation(summary = "Get measurements for a device within a time range")
    @GetMapping("/device/{deviceId}/measurements/range")
    public ResponseEntity<List<DeviceMeasurementDTO>> getDeviceMeasurementsInRange(
            @PathVariable UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<DeviceMeasurementDTO> measurements = loadBalancingService.getDeviceMeasurementsInRange(deviceId, start, end);
        return ResponseEntity.ok(measurements);
    }

    @Operation(summary = "Get hourly consumption for a device")
    @GetMapping("/device/{deviceId}/hourly")
    public ResponseEntity<List<HourlyConsumptionDTO>> getHourlyConsumption(@PathVariable UUID deviceId) {
        List<HourlyConsumptionDTO> consumption = loadBalancingService.getHourlyConsumption(deviceId);
        return ResponseEntity.ok(consumption);
    }

    @Operation(summary = "Get hourly consumption within a time range")
    @GetMapping("/device/{deviceId}/hourly/range")
    public ResponseEntity<List<HourlyConsumptionDTO>> getHourlyConsumptionInRange(
            @PathVariable UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<HourlyConsumptionDTO> consumption = loadBalancingService.getHourlyConsumptionInRange(deviceId, start, end);
        return ResponseEntity.ok(consumption);
    }

    @Operation(summary = "Get all devices that exceeded max consumption")
    @GetMapping("/exceeded")
    public ResponseEntity<List<HourlyConsumptionDTO>> getExceededConsumption() {
        List<HourlyConsumptionDTO> exceeded = loadBalancingService.getAllExceededConsumption();
        return ResponseEntity.ok(exceeded);
    }

    @Operation(summary = "Get exceeded consumption for a specific device")
    @GetMapping("/device/{deviceId}/exceeded")
    public ResponseEntity<List<HourlyConsumptionDTO>> getDeviceExceededConsumption(@PathVariable UUID deviceId) {
        List<HourlyConsumptionDTO> exceeded = loadBalancingService.getDeviceExceededConsumption(deviceId);
        return ResponseEntity.ok(exceeded);
    }

    @Operation(summary = "Get consumption statistics for a device")
    @GetMapping("/device/{deviceId}/stats")
    public ResponseEntity<ConsumptionStatsDTO> getConsumptionStats(
            @PathVariable UUID deviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        ConsumptionStatsDTO stats = loadBalancingService.getConsumptionStats(deviceId, start, end);
        if (stats == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stats);
    }

    @Operation(summary = "Manually process a new measurement")
    @PostMapping("/measurement")
    public ResponseEntity<String> processMeasurement(@Valid @RequestBody DeviceMeasurementDTO measurement) {
        loadBalancingService.processMeasurement(measurement);
        return ResponseEntity.ok("Measurement processed successfully");
    }

    @Operation(summary = "Get all hourly consumption records")
    @GetMapping("/hourly/all")
    public ResponseEntity<List<HourlyConsumptionDTO>> getAllHourlyConsumption() {
        List<HourlyConsumptionDTO> consumption = loadBalancingService.getAllHourlyConsumption();
        return ResponseEntity.ok(consumption);
    }

    @Operation(summary = "Health check endpoint")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Monitoring Service is running");
    }
}