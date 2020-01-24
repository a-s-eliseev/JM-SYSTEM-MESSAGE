package jm;

import java.io.IOException;

public interface GitMethodsService {

    String getRepositories(String login, String password) throws IOException;
    String getCommits(String login, String repository);
}
