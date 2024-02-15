package com.congestion.test.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaxControllerTest {

  private static String CITY_NAME1 = "Gothenburg";
  private static String CITY_NAME2 = "Kyiv";
  private static String CITY_NAME3 = "Lviv";

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testSetCityCongestionTollRules() throws Exception {
    String requestBody = "{ \"city\": \"" + CITY_NAME1 + "\", \"otherFields\": \"values\", \"dateformat\": \"yyyy-MM-dd" +
      " HH:mm:ss\" }";

    mockMvc.perform(put("/tax-calculation-city-rules")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(requestBody))
      .andExpect(status().isOk()) // or isOk() based on isNewEntry
      .andExpect(jsonPath("$.city").value("Gothenburg"))
      .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  void testRepeatedSetCityCongestionTollRules() throws Exception {
    String requestBody = "{ \"city\": \"" + CITY_NAME2 + "\", \"otherFields\": \"values\", \"dateformat\": \"yyyy-MM-dd" +
      " HH:mm:ss\" }";

    mockMvc.perform(put("/tax-calculation-city-rules")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(requestBody))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.city").value(CITY_NAME2))
      .andExpect(jsonPath("$.statusMessage").value(buildStatusMessage("created", CITY_NAME2)))
      .andExpect(jsonPath("$.success").value(true));

    mockMvc.perform(put("/tax-calculation-city-rules")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(requestBody))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.statusMessage").value(buildStatusMessage("updated", CITY_NAME2)))
      .andExpect(jsonPath("$.city").value(CITY_NAME2))
      .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  void testCalcCongestionTaxForCarFailedDueToMissingRequiredParams() throws Exception {
    // Ensure the city toll rules are set first
    mockMvc.perform(put("/tax-calculation-city-rules")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content("{ \"city\": \"" + CITY_NAME3 + "\" }"))
      .andExpect(status().isBadRequest());
  }

  @Test
  void testCalcCongestionTaxForCarSuccess() throws Exception {
    // Ensure the city toll rules are set first
    mockMvc.perform(put("/tax-calculation-city-rules")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content( "{ \"city\": \"" + CITY_NAME3 + "\", \"dateformat\": \"yyyy-MM-dd HH:mm:ss\" }"))
      .andExpect(status().isOk());

    String requestBody = "{ \"city\": \"" + CITY_NAME3 + "\", \"vehicle\": { \"type\": \"Car\" }, \"passages\": " +
      "[\"2013-01-01 08:00:00\"] }";

    mockMvc.perform(post("/congestion-tax-calculation")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(requestBody))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.congestionTax").isNumber())
      .andExpect(jsonPath("$.success").value(true));
  }

  private String buildStatusMessage(String status, String city) {
    return String.format("The toll rules were %s for the city %s", status, city);
  }

}