package subtitle.restapi.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import subtitle.base.restapi.AbstractApiService;
import subtitle.restapi.ui.model.UserDto;

@RestController
@RequestMapping("api/user/profile")
public class ApiUserController extends AbstractApiService {

    @GetMapping("/me")
    public UserDto me() {
        try {
            return new UserDto(getCurrentUser());
        } catch (Exception e) {
            throw getException(e, "Can`t get user");
        }
    }
}
