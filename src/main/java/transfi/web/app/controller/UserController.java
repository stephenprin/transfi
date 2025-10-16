package transfi.web.app.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import transfi.web.app.dto.UserDto;
import transfi.web.app.entity.User;
import transfi.web.app.service.UserService;



@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        User registeredUser = userService.regiserUser(userDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authenticateUser( @RequestBody UserDto userDto) {
        var authObject = userService.authenticateUser(userDto);
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authObject.get("token").toString()).body(authObject.get("user"));
    }

}
