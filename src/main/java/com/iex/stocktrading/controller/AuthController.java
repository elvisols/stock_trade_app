package com.iex.stocktrading.controller;

import com.iex.stocktrading.helper.MapValidationErrorHandler;
import com.iex.stocktrading.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static com.iex.stocktrading.config.Constants.TOKEN_PREFIX;


@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private JwtTokenProvider tokenProvider;

    private AuthenticationManager authenticationManager;

    private MapValidationErrorHandler handle;

    public AuthController(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager, MapValidationErrorHandler handle) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.handle = handle;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?> errorMap = handle.MapValidationService(result);
        if(errorMap != null) return errorMap;


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = TOKEN_PREFIX +  tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTResponse(true, jwt));
    }
}

@Getter
@Setter
@AllArgsConstructor
class JWTResponse {
    private boolean status;
    private String token;
}

@Getter @Setter
class LoginRequest {

    @NotBlank(message = "***> Username cannot be blank")
    private String username;

    @NotBlank(message = "***> Password cannot be blank")
    private String password;

}