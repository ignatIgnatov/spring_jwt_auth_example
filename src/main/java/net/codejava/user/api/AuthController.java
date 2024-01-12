package net.codejava.user.api;

import javax.validation.Valid;

import net.codejava.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import net.codejava.jwt.JwtTokenUtil;
import net.codejava.user.UserEntity;

@RestController
public class AuthController {
  @Autowired AuthenticationManager authManager;
  @Autowired PasswordEncoder passwordEncoder;
  @Autowired JwtTokenUtil jwtUtil;
  @Autowired
  UserRepository userRepository;

  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDto request) {
    try {
      Authentication authentication =
          authManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

      UserEntity user = (UserEntity) authentication.getPrincipal();
      String accessToken = jwtUtil.generateAccessToken(user);
      AuthResponseDto response = new AuthResponseDto(user.getEmail(), accessToken);

      return ResponseEntity.ok().body(response);

    } catch (BadCredentialsException ex) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @PostMapping("auth/register")
  public ResponseEntity<?> register(@RequestBody @Valid UserRequestDto userRequestDto) {
    UserEntity user = new UserEntity();
    UserResponseDto userResponseDto = new UserResponseDto();

    String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
    user.setEmail(userRequestDto.getEmail());
    user.setPassword(encodedPassword);
    userRepository.save(user);
    userResponseDto.setEmail(userRequestDto.getEmail());
    return ResponseEntity.ok().body(userResponseDto);
  }
}
