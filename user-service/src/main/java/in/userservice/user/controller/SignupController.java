package in.userservice.user.controller;

import in.userservice.user.dto.UserDTO;
import in.userservice.user.dto.UserSignupDTO;
import in.userservice.user.service.EmailService;
import in.userservice.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class SignupController {

    private final EmailService emailService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @GetMapping("/signup")
    public ModelAndView showSignupForm(ModelAndView model) {
        model.addObject("userDTO", new UserDTO());
        model.setViewName("signup");
        return model;
    }

    @PostMapping("/send-verification-email")
    public ResponseEntity<Map<String, String>> sendVerificationEmail(@RequestParam String email) {

        emailService.sendVerificationEmail(email);

        Map<String, String> response = new HashMap<>();
        response.put("redirect", "/signup");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Map<String, String>> verifyCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        Map<String, String> response = new HashMap<>();
        if (emailService.verifyCode(email, code)) {
            response.put("emailVerified", "true");
            response.put("successMessage", "Email verified! You can now complete your registration.");
        } else {
            response.put("emailVerified", "false");
            response.put("errorMessage", "Invalid verification code.");
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping("/signup")
    public String signup(@RequestPart("userDTO") UserSignupDTO userDTO,
                         @RequestPart("profile") MultipartFile profileFile,
                         HttpServletResponse response) throws Exception {


        Map<String,String> map = userService.registerUser(userDTO, profileFile);

        Cookie cookie = new Cookie("stockJwtToken", map.get("jwtToken"));
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setPath("/");

        // 리프레시 토큰을 쿠키에 저장
        Cookie refreshTokenCookie = new Cookie("stockRefreshToken", map.get("refreshToken"));
        refreshTokenCookie.setHttpOnly(false);
        refreshTokenCookie.setSecure(false); // HTTPS에서만 사용
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        refreshTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);
        response.addCookie(cookie);


        return "main";  // main 페이지로 리다이렉트
    }
}
