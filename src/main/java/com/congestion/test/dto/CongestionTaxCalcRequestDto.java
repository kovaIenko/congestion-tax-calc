package com.congestion.test.dto;

import com.congestion.test.vehicles.Vehicle;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class CongestionTaxCalcRequestDto {

  @NonNull
  @JsonProperty(required = true)
  private Vehicle vehicle;

  private List<String> passages = new ArrayList<>();

  @NonNull
  @JsonProperty(required = true)
  private String city;

}
