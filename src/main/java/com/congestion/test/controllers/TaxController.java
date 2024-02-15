package com.congestion.test.controllers;

import com.congestion.test.dto.CityToRulesResponseDto;
import com.congestion.test.dto.CityTollRulesRequestDto;
import com.congestion.test.dto.CongestionTaxCalcRequestDto;
import com.congestion.test.dto.CongestionTaxResponseDto;
import com.congestion.test.services.CongestionTaxCalculatorService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@Slf4j
public class TaxController {

  private final CongestionTaxCalculatorService congestionTaxCalculatorService;

  private final Map<String, CityTollRulesRequestDto> citiesTollRules = new HashMap<>();

  @PutMapping("/tax-calculation-city-rules")
  public ResponseEntity<CityToRulesResponseDto> setCityCongestionTollRules(@NonNull @RequestBody CityTollRulesRequestDto cityTollRulesRequestDto) {
    String city = cityTollRulesRequestDto.getCity();
    boolean isNewEntry = !citiesTollRules.containsKey(city);

    citiesTollRules.put(city, cityTollRulesRequestDto);

    String status = isNewEntry ? "created" : "updated";
    final String statusMessage = String.format("The toll rules were %s for the city %s", status, city);
    log.info(statusMessage);
    return ResponseEntity.status(HttpStatus.OK)
      .body(new CityToRulesResponseDto(city, true, statusMessage));
  }

  @PostMapping("/congestion-tax-calculation")
  public ResponseEntity<CongestionTaxResponseDto> calcCongestionTax(@NonNull @RequestBody final CongestionTaxCalcRequestDto congestionTaxCalcRequestDto) {
    if (!citiesTollRules.containsKey(congestionTaxCalcRequestDto.getCity())) {
      log.info("The API does not have the valid toll rules for city {}", congestionTaxCalcRequestDto.getCity());
      return ResponseEntity.ok(
        new CongestionTaxResponseDto(
          congestionTaxCalcRequestDto.getCity(),
          congestionTaxCalcRequestDto.getVehicle().getType().getType(),
          -1,
          false,
          String.format(
            "The API does not have the valid toll rules for city %s. Please initialize it with us via PUT " +
              "/tax-calculation-city-rules",
            congestionTaxCalcRequestDto.getCity()
          )
        ));
    }

    var cityTollRulesDto = citiesTollRules.get(congestionTaxCalcRequestDto.getCity());
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(cityTollRulesDto.getDateformat());

    final int taxCalc = congestionTaxCalculatorService.getTax(
      congestionTaxCalcRequestDto.getVehicle(),
      congestionTaxCalcRequestDto.getPassages()
        .stream()
        .map(passage -> LocalDateTime.parse(passage, dateFormat))
        .toList(),
      cityTollRulesDto.getPublicHolidays(),
      cityTollRulesDto.getCityTollPeriods(),
      cityTollRulesDto.getFreeVehiclesTypes()
    );

    final String statusMessage = String.format(
      "The congestion tax for the type of vehicle %s in city %s equals %s.",
      congestionTaxCalcRequestDto.getVehicle().getType(),
      cityTollRulesDto.getCity(),
      taxCalc
    );
    log.info(statusMessage);
    return ResponseEntity.ok(new CongestionTaxResponseDto(
      congestionTaxCalcRequestDto.getCity(),
      congestionTaxCalcRequestDto.getVehicle().getType().getType(),
      taxCalc,
      true,
      statusMessage
    ));
  }

  @GetMapping("/health")
  public ResponseEntity<String> isHealthy() {
    return new ResponseEntity<>("We are healthy", HttpStatus.OK);
  }

}
