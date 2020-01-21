package jm.github;

import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.OAuthService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

public class GistViaOAuthToken {
    public static void main(String[] args) throws IOException {

        final String format = "{0}) {1}- created on {2}";
        int count = 1;
        final int size = 25;
        final RepositoryId repository = new RepositoryId("NikitaNesterenko", "JM-SYSTEM-MESSAGE");
        final String message = "   {0} by {1} on {2}";
        int pages = 1;

        GitHubClient client = new GitHubClient();
        client.setCredentials("dane.witcher@yandex.ru", "Gamdalf1988");
        RepositoryService repoService = new RepositoryService(client);
        CommitService commitService = new CommitService(client);
        List<Repository> repos = repoService.getRepositories();
        for (Repository repo : repos)
            System.out.println(MessageFormat.format(format, count++,
                    repo.getName(), repo.getCreatedAt()));

        for (Collection<RepositoryCommit> commits : commitService.pageCommits(repository, size)) {
            System.out.println("Commit Page " + pages++);
            for (RepositoryCommit commit : commits) {
                String sha = commit.getSha().substring(0, 7);
                String author = commit.getCommit().getAuthor().getName();
                Date date = commit.getCommit().getAuthor().getDate();
                System.out.println(MessageFormat.format(message, sha, author,
                        date));
            }
        }



//        OAuthService oauthService = new OAuthService();
//
//        // Replace with actual login and password
//        oauthService.getClient().setCredentials("dane.witcher@yandex.ru", "Gamdalf1988");
//
//        // Create authorization with 'gist' scope only
//        Authorization auth = new Authorization();
//        auth.setScopes(Arrays.asList("gist"));
//        auth = oauthService.createAuthorization(auth);
//
//        // Create Gist service configured with OAuth2 token
//        GistService gistService = new GistService();
//        gistService.getClient().setOAuth2Token(auth.getToken());
//
//        // Create Gist
//        Gist gist = new Gist();
//        gist.setPublic(false);
//        gist.setDescription("Created using OAuth2 token via Java API");
//        GistFile file = new GistFile();
//        file.setContent("Gist!");
//        file.setFilename("gist.txt");
//        gist.setFiles(Collections.singletonMap(file.getFilename(), file));
//        gist = gistService.createGist(gist);
//        System.out.println("Created Gist at " + gist.getHtmlUrl());
    }
}
