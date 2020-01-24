package jm;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

@Service
@Transactional
public class GitMethodsServiceImpl implements GitMethodsService {
    final String format = "{0}) {1}- created on {2}";
    int count = 1;
    final int size = 25;
    final String message = "   {0} by {1} on {2}";
    int pages = 1;

    @Override
    public String getRepositories(String login, String password) throws IOException {
        GitHubClient client = new GitHubClient();
        client.setCredentials(login, password);
        RepositoryService repoService = new RepositoryService(client);
        CommitService commitService = new CommitService(client);
        List<Repository> repos = repoService.getRepositories();
        String result  = StringUtils.join(repos, "|");
        for (Repository repo : repos)
            System.out.println(MessageFormat.format(format, count++,
                    repo.getName(), repo.getCreatedAt()));
        return result;

    }
    @Override
    public String getCommits(String login, String repository) {
        final CommitService service = new CommitService();
        final RepositoryId repo = new RepositoryId(login, repository);
        String result = "";
        for (Collection<RepositoryCommit> commits : service.pageCommits(repo,
                size)) {
            System.out.println("Commit Page " + pages++);
            for (RepositoryCommit commit : commits) {
                String sha = commit.getSha().substring(0, 7);
                String author = commit.getCommit().getAuthor().getName();
                Date date = commit.getCommit().getAuthor().getDate();
                String comm = MessageFormat.format(message, sha, author, date);
                System.out.println(comm);
                StringBuffer sb = new StringBuffer(result);
                result = sb.toString();


            }

        }

        return result;
    }
}

