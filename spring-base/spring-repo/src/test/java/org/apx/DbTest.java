package org.apx;

import junit.framework.Assert;
import org.apx.model.User;
import org.apx.repo.CommonRepo;
import org.apx.repo.utils.Paging;
import org.apx.repo.utils.Sorting;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 11.03.14
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:db.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class DbTest {


    @Autowired
    CommonRepo repo;

    @Test
    public void testConnection(){
        Assert.assertTrue(repo.checkConnection());
    }


    @Test
    @Rollback
    public void createUser(){
        User user = new User();
        User user2 = null;
        user.setDisplayName("Test User One");
        user.setInternalName("Test User One");
        user2 = repo.save(user);
        Assert.assertEquals(user,user2);
        Assert.assertEquals(1,repo.getResultList(User.class).size());
        Assert.assertEquals(1, repo.getResultList(User.class, null, null, Paging.init(0,10),Sorting.init("displayName").put("id")).size());

    }
}
