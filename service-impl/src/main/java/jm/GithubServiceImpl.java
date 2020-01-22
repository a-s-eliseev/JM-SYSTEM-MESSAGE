package jm;

import jm.model.Application;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
@Transactional
public class GithubServiceImpl implements GithubService {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void createApp(Application application) {
        entityManager.persist(application);
    }
}
