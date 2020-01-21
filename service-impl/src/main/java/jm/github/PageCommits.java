package jm.github;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Date;

public class PageCommits {
    public static void main(String[] args) throws IOException {

        final int size = 25;
        final RepositoryId repo = new RepositoryId("NikitaNesterenko", "JM-SYSTEM-MESSAGE");
        final String message = "   {0} by {1} on {2}";
        final CommitService service = new CommitService();
        int pages = 1;
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
    }
}
