package jm;

import java.io.IOException;

public interface GitMethodsService {

    String getRepositories() throws IOException;
    String getCommits();
}
