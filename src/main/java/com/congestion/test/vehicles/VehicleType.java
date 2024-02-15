package com.congestion.test.vehicles;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum VehicleType {

  CAR("Car"),
  DIPLOMAT("Diplomat"),
  EMERGENCY("Emergency"),
  FOREIGN("Foreign"),
  MILITARY("Military"),
  MOTORBIKE("Motorbike"),
  MOTORCYCLE("Motorcycle"),
  TRACTOR("Tractor");

  private final String type;


  @JsonValue // This annotation tells Jackson to use this value in serialization
  public String getType() {
    return type;
  }

  @JsonCreator // This annotation tells Jackson to use this method for deserialization
  public static VehicleType forValue(String value) {
    for (VehicleType vehicleType : values()) {
      if (vehicleType.getType().equalsIgnoreCase(value)) {
        return vehicleType;
      }
    }
    throw new IllegalArgumentException("Unknown vehicle type: " + value);
  }

}
