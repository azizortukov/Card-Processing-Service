package uz.anas.card.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.anas.card.exceptions.ExceptionResponse;
import uz.anas.card.model.dto.CardResponseDto;
import uz.anas.card.model.dto.CreateCardDto;
import uz.anas.card.service.CardService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class CardController {

    private final CardService cardService;

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(tags = "Card creation API",
            description = "Fetches a paginated and sorted list of tasks for a user identified by the provided ID. Supports pagination and sorting parameters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Card created successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDto.class))),
            @ApiResponse(
                    responseCode = "200",
                    description = "Card with this Idempotency-Key has already been created before. API returns current card data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Missing field, invalid data or card limit is exceeded",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Request sent without authorization/token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User doesn't have privilege to access this resource",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    public HttpEntity<?> saveCard(@RequestHeader("Idempotency-Key") UUID idempotencyKey,
                                  @RequestBody @Valid CreateCardDto cardDto) {
        return cardService.createNewCard(idempotencyKey, cardDto);
    }

}
