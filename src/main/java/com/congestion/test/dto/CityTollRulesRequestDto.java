package com.congestion.test.dto;


import com.congestion.test.vehicles.VehicleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CityTollRulesRequestDto {

  @NonNull
  @JsonProperty(required = true)
  private String city;

  @JsonProperty("city_toll_periods")
  private List<TollPeriodDto> cityTollPeriods = new ArrayList<>();

  @JsonProperty("public_holidays")
  private List<String> publicHolidays = new ArrayList<>();

  @NonNull
  private String dateformat;

  @JsonProperty("free_vehicle_types")
  private Set<VehicleType> freeVehiclesTypes = new HashSet<>();

}
