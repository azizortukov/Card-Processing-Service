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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.anas.card.exceptions.ExceptionResponse;
import uz.anas.card.model.dto.LoginDTO;
import uz.anas.card.model.dto.SignUpDTO;
import uz.anas.card.model.dto.TokenDTO;
import uz.anas.card.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API", description = "(For getting access token)")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Login API", description = "API for getting access token.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Token is given",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "400", description = "Email format is wrong or password length is small (at least 8 characters)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody @Valid LoginDTO loginDto) {
        return userService.checkLoginDetails(loginDto);
    }

    @Operation(summary = "Sign up API", description = "API for registration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered as client"),
            @ApiResponse(responseCode = "400", description = """
                    Email format is wrong, password length is small (at least 8 characters) or user already registered""",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @PostMapping("/sign_up")
    public HttpEntity<?> signUp(@RequestBody @Valid SignUpDTO signUpDTO) {
        return userService.singUp(signUpDTO);
    }
}