package jm;

import jm.GitMethodsService;
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
    final RepositoryId repository = new RepositoryId("NikitaNesterenko", "JM-SYSTEM-MESSAGE");
    final String message = "   {0} by {1} on {2}";
    int pages = 1;

    @Override
    public String getRepositories() throws IOException {
        GitHubClient client = new GitHubClient();
        client.setCredentials("dane.witcher@yandex.ru", "Gamdalf1988");
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
    public String getCommits() {
        final CommitService service = new CommitService();
        final RepositoryId repo = new RepositoryId("NikitaNesterenko", "JM-SYSTEM-MESSAGE");


        for (Collection<RepositoryCommit> commits : service.pageCommits(repo,
                size)) {
            System.out.println("Commit Page " + pages++);
            for (RepositoryCommit commit : commits) {
                String sha = commit.getSha().substring(0, 7);
                String author = commit.getCommit().getAuthor().getName();
                Date date = commit.getCommit().getAuthor().getDate();
                System.out.println(MessageFormat.format(message, sha, author,
                        date));
            }
        }
        String result = "";
        return result;
    }




}

