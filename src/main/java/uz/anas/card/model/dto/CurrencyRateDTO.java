package uz.anas.card.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrencyRateDTO(@JsonProperty("Rate") double rate) {}
