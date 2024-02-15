package com.congestion.test.dto;

public record CongestionTaxResponseDto(String city, String vehicleType, int congestionTax, boolean success, String statusMessage) {
}
