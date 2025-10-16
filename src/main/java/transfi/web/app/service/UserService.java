package transfi.web.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import transfi.web.app.dto.UserDto;
import transfi.web.app.entity.User;
import transfi.web.app.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
  public class UserService {

      private final UserRepository userRepository;
      private final PasswordEncoder passwordEncoder;
      private final UserDetailsService userDetailsService;
       private final AuthenticationManager authenticationManager;
       private final JwtService jwtService;

      public User regiserUser(UserDto userDto) {
            User user = mapToUser(userDto);
          return userRepository.save(user);
      }

    public Map<String, Object> authenticateUser(UserDto userDto) {
          Map<String, Object> authObject = new HashMap<String, Object>();
          User user = (User) userDetailsService.loadUserByUsername(userDto.getUsername());
          if( user==null){
              throw new UsernameNotFoundException("Invalid username or password.");
          }
          authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
          authObject.put("token", "Bearer ".concat(jwtService.generateToken(userDto.getUsername())));
            authObject.put("user", user);
            return authObject;

    }
      public User mapToUser(UserDto userDto) {
          return  User.builder()
                  .firstname(userDto.getFirstname())
                  .lastname(userDto.getLastname())
                  .username(userDto.getUsername())
                  .dob(userDto.getDob())
                  .tel(userDto.getTel())
                  .tag("ionic" + userDto.getUsername())
                  .dob(userDto.getDob())
                  .password(passwordEncoder.encode(userDto.getPassword()))
                  .roles(List.of("USER"))
                  .build();
      }

}

