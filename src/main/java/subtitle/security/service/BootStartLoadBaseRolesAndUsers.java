package subtitle.security.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subtitle.base.exception.SubtitleException;
import subtitle.base.startup.DbUpdateAfterStart;
import subtitle.restapi.ui.model.SignupRequest;
import subtitle.security.model.Authority;
import subtitle.security.model.Role;
import subtitle.security.model.User;
import subtitle.security.repository.AuthorityRepository;

@Service
@Log
public class BootStartLoadBaseRolesAndUsers implements DbUpdateAfterStart {

    private static final String DB_UPDATE_NAME = "LOAD_ROLES_AND_ADMIN";
    private final AuthorityRepository authorityRepository;
    private final UserService userService;

    @Autowired
    public BootStartLoadBaseRolesAndUsers(AuthorityRepository authorityRepository, UserService userService){
        this.authorityRepository = authorityRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public void dbUpdate() {
        Authority roleAdmin = authorityRepository.save(new Authority(Role.ROLE_ADMIN));
        authorityRepository.save(new Authority(Role.ROLE_USER));
        User admin;
        try {
            admin = userService.signup(new SignupRequest("admin", "admin@admin.ru", "admin"));
            admin.getAuthoritiesSet().add(roleAdmin);
        } catch (SubtitleException e) {
            throw new IllegalStateException("Can`t create admin user");
        }
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public String getName() {
        return DB_UPDATE_NAME;
    }
}
