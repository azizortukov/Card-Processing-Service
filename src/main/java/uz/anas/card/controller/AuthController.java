package uz.anas.card.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.anas.card.model.dto.UserDto;
import uz.anas.card.service.JwtService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "(For Sign Up, Login, sending verification code)")
public class AuthController {

    private final JwtService jwtService;

    @Operation(
            summary = "Login API",
            description = """
            This API receives user details, please check for validity before requesting this API. The responses are
            200(success) with access & refresh tokens, 401(unauthorized) if entered user details are wrong
            """)
    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody @Valid UserDto userDto) {
        return jwtService.checkLoginDetails(userDto);
    }
}
