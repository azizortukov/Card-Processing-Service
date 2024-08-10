package uz.anas.card.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyRateDto(@JsonProperty("Rate") double rate) {}
