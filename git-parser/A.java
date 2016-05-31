package ua.nure.nikolaienko;

/**
 * Created by Vladyslav_Nikolaienk on 5/31/2016.
 */
public class A {

    //TODO: Change logger
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserDao userDao;

    //TODO: find any name
    public User getUserById(long pId) {
        LOG.info("Inside #getUserById : (pId = {})", pId);
        User user = userDao.getUser(pId);
        LOG.info("Leaving #getUserById : (user = {})", user);
        return user;
    }

}
