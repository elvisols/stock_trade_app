package com.iex.stocktrading.controller;

import com.iex.stocktrading.exception.UserNotFoundException;
import com.iex.stocktrading.helper.ResponseWrapper;
import com.iex.stocktrading.helper.UserValidator;
import com.iex.stocktrading.model.dto.NewUserDTO;
import com.iex.stocktrading.model.dto.UserDTO;
import com.iex.stocktrading.helper.MapValidationErrorHandler;
import com.iex.stocktrading.security.JwtTokenProvider;
import com.iex.stocktrading.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import java.util.Optional;

import static com.iex.stocktrading.config.Constants.TOKEN_PREFIX;


@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private Queue queue;

    private UserService userService;

    private JwtTokenProvider tokenProvider;

    private JmsMessagingTemplate jmsMessagingTemplate;

    private AuthenticationManager authenticationManager;

    private MapValidationErrorHandler mapValidationErrorHandler;

    private UserValidator userValidator;

    public AuthController(Queue queue, UserService userService, JwtTokenProvider tokenProvider, JmsMessagingTemplate jmsMessagingTemplate, AuthenticationManager authenticationManager, MapValidationErrorHandler mapValidationErrorHandler, UserValidator userValidator) {
        this.queue = queue;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.jmsMessagingTemplate = jmsMessagingTemplate;
        this.authenticationManager = authenticationManager;
        this.mapValidationErrorHandler = mapValidationErrorHandler;
        this.userValidator = userValidator;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorHandler.MapValidationService(result);
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

    @GetMapping
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        Page<UserDTO> userDTOs = userService.findAll(pageable);

        System.out.println(userDTOs);

        return new ResponseEntity<ResponseWrapper>(new ResponseWrapper(userDTOs), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        Optional<UserDTO> userDTO = userService.findOne(id);

        if (!userDTO.isPresent()) {
            throw new UserNotFoundException("User not found for id: " + id);
        }

        return userDTO.get();
    }

    @PostMapping
    public ResponseEntity<?> register(@Valid @RequestBody NewUserDTO user, BindingResult result){
        // Validate passwords match
        userValidator.validate(user,result);

        ResponseEntity<?> errorMap = mapValidationErrorHandler.MapValidationService(result);
        if(errorMap != null)return errorMap;

        UserDTO newUser = userService.save(user);

        return  new ResponseEntity<UserDTO>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserDTO user, BindingResult bindingResult) {
        ResponseEntity<?> errorMap = mapValidationErrorHandler.MapValidationService(bindingResult);

        if(errorMap != null) return errorMap;

        user.setId(id);
        UserDTO p = userService.update(user);

        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);

        return new ResponseEntity<String>("User deleted! :(", HttpStatus.OK);
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

    @NotBlank(message = "*Username cannot be blank")
    private String username;

    @NotBlank(message = "*Password cannot be blank")
    private String password;

}