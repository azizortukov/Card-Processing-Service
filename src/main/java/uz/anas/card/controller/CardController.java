package uz.anas.card.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.anas.card.exceptions.ExceptionResponse;
import uz.anas.card.model.dto.*;
import uz.anas.card.service.CardService;

import java.util.UUID;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
@Tag(name = "Cards API", description = "(CRUD operations and search mechanism)")
public class CardController {

    private final CardService cardService;

    @Operation(description = "Creates new card with given characteristics")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Card created successfully.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDTO.class))),
            @ApiResponse(
                    responseCode = "200",
                    description = "Card with this Idempotency-Key has already been created before. API returns current card data.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Missing field, invalid data or card limit is exceeded",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Request sent without authorization/token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User doesn't have privilege to access this resource",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "User cannot be found by provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping
    public HttpEntity<?> saveCard(
            @RequestHeader("Idempotency-Key") UUID idempotencyKey,
            @RequestBody @Valid CardRequestDTO cardDto) {
        return cardService.createNewCard(idempotencyKey, cardDto);
    }

    @Operation(description = "Retrieve a card by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Card found and returned",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID Format is wrong",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Request sent without authorization/token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User doesn't have privilege to access this resource",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Card not found with provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @GetMapping("/{cardId}")
    public HttpEntity<?> getCard(@PathVariable UUID cardId) {
        return cardService.getCardById(cardId);
    }

    @Operation(description = "Block a card by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Card status changed to BLOCKED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID format is wrong, card is not active or ETag format is wrong",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Request sent without authorization/token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User doesn't have privilege to access this resource",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Card not found with provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/{cardId}/block")
    public HttpEntity<?> blockCard(
            @RequestHeader("If-Match") String eTag,
            @PathVariable UUID cardId) {
        return cardService.blockCard(eTag, cardId);
    }

    @Operation(description = "Re active a card by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Card status changed to ACTIVE",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DebitResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID format is wrong, missing param or card is not BLOCKED",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Request sent without authorization/token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User doesn't have privilege to access this resource",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Card not found with provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/{cardId}/unblock")
    public HttpEntity<?> activeCard(
            @RequestHeader("If-Match") String eTag,
            @PathVariable UUID cardId) {
        return cardService.activeCard(eTag, cardId);
    }

    @Operation(description = "Withdraw a fund by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Money successfully withdrawn",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID format is wrong, card is not active, insufficient fund or ETag format is wrong",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Request sent without authorization/token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User doesn't have privilege to access this resource",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Card not found with provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/{cardId}/debit")
    public HttpEntity<?> debitCard(
            @RequestHeader("Idempotency-Key") UUID idempotencyKey,
            @PathVariable UUID cardId,
            @RequestBody @Valid DebitRequestDTO debitRequestDTO) {
        return cardService.sendMoney(idempotencyKey, cardId, debitRequestDTO);
    }

    @Operation(description = "Deposit a fund by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Money successfully deposited",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "ID format is wrong, card is not active or ETag format is wrong",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "Request sent without authorization/token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "User doesn't have privilege to access this resource",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Card not found with provided ID",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/{cardId}/credit")
    public HttpEntity<?> creditCard(
            @RequestHeader("Idempotency-Key") UUID idempotencyKey,
            @PathVariable UUID cardId,
            @RequestBody @Valid CreditRequestDTO creditRequestDTO) {
        return cardService.receiveMoney(idempotencyKey, cardId, creditRequestDTO);
    }

}
