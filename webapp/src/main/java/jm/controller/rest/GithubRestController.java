package jm.controller.rest;


import jm.GithubService;
import jm.UserService;
import jm.dto.ApplicationDTO;
import jm.dto.ApplicationDTOService;
import jm.dto.ChannelDTO;
import jm.dto.ChannelDtoService;
import jm.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/github")
public class GithubRestController {

    private UserService userService;

    private GithubService githubService;

    private ApplicationDTOService applicationDTOService;

    private static final Logger logger = LoggerFactory.getLogger(
            GithubRestController.class);

    @Autowired
    public void  setApplicationDTOService(ApplicationDTOService applicationDTOService) {
        this.applicationDTOService = applicationDTOService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setGithubService(GithubService githubService) {
        this.githubService = githubService;
    }

    @PostMapping("/token")
    public ResponseEntity githubtoken(@RequestBody GitHubUsers githubToken, HttpServletRequest request)  {
        request.getSession().setAttribute("token", githubToken);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ApplicationDTO> createApp(Principal principal, @RequestBody ApplicationDTO applicationDTO, HttpServletRequest request) {
        Application application = applicationDTOService.toEntity(applicationDTO);
        if (principal != null) {
            User owner = userService.getUserByLogin(principal.getName());
            Workspace workspace = (Workspace) request.getSession().getAttribute("WorkspaceID");

            application.setUser(owner);
            application.setWorkspace(workspace);
        }
        try {
            githubService.createApp(application);
            logger.info("Cозданный application: {}", application);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            logger.warn("Не удалось создать application");
            ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(applicationDTO, HttpStatus.OK);
    }

}
