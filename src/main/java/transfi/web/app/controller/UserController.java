package transfi.web.app.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import transfi.web.app.dto.UserDto;
import transfi.web.app.entity.User;
import transfi.web.app.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/register")
    public ResponseEntity<User> registerUser(UserDto userDto) {
        User registeredUser = userService.regiserUser(userDto);
        return ResponseEntity.ok(registeredUser);
    }

    public ResponseEntity<?> authenticateUser(UserDto userDto) {
        var authObject = userService.authenticateUser(userDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION).body((String) authObject.get("user"));
    }

}
