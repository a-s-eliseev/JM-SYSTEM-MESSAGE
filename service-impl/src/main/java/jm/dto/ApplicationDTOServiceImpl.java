package jm.dto;

import jm.BotService;
import jm.UserService;
import jm.WorkspaceService;
import jm.model.Application;
import jm.model.Bot;
import jm.model.Channel;
import jm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApplicationDTOServiceImpl implements ApplicationDTOService {

    @Autowired
    UserService userService;

    @Autowired
    BotService botService;

    @Autowired
    WorkspaceService workspaceService;

    @Override
    public ApplicationDTO toDto(Application application) {
        if (application == null) {
            return null;
        }

        ApplicationDTO applicationDTO = new ApplicationDTO();

        Set<Long> userIds = application.getUsers().stream().map(User::getId).collect(Collectors.toSet());
        applicationDTO.setUserIds(userIds);

        Set<Long> botIds = application.getBots().stream().map(Bot::getId).collect(Collectors.toSet());
        applicationDTO.setBotIds(botIds);

        applicationDTO.setId(application.getId());
        applicationDTO.setName(application.getName());
        applicationDTO.setWorkspaceId(application.getWorkspace().getId());
        applicationDTO.setOwnerId(application.getUser().getId());
        applicationDTO.setIsPrivate(application.getIsPrivate());


        return applicationDTO;
    }

    @Override
    public List<ApplicationDTO> toDto(List<Application> applications) {
        if (applications==null) {
            return null;
        }

        List<ApplicationDTO> applicationlDTOList = new ArrayList<>();
        for (Application application : applications) {
            applicationlDTOList.add(toDto(application));
        }
        return applicationlDTOList;
    }

    @Override
    public Application toEntity(ApplicationDTO applicationDTO ) {
        if (applicationDTO==null) {
            return null;
        }

        Application channel = new Application();

        if (applicationDTO.getUserIds()!=null) {
            Set<User> userSet = new HashSet<>();
            for (Long id : applicationDTO.getUserIds()) {
                userSet.add(userService.getUserById(id));
            }
            channel.setUsers(userSet);
        }

        if (applicationDTO.getBotIds()!=null) {
            Set<Bot> botSet = new HashSet<>();
            for (Long id : applicationDTO.getBotIds()) {
                botSet.add(botService.getBotById(id));
            }
            channel.setBots(botSet);
        }

        if (applicationDTO.getWorkspaceId()!=null) {
            channel.setWorkspace(
                    workspaceService.getWorkspaceById(applicationDTO.getWorkspaceId()));
        }
        User userOwner = userService.getUserById(applicationDTO.getOwnerId());


        channel.setId(applicationDTO.getId());
        channel.setName(applicationDTO.getName());
        channel.setUser(userOwner);
        channel.setIsPrivate(applicationDTO.getIsPrivate());
        channel.setArchived(false);
        channel.setCreatedDate(applicationDTO.getCreatedDate());

        return channel;
    }
}
