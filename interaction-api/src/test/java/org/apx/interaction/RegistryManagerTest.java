package org.apx.interaction;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14.04.14
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration("classpath:interaction.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RegistryManagerTest {

    @Test
    public void testReconnect() throws InterruptedException {
        while(!RegistryManager.instance().isActive()){
            Thread.sleep(2000);
        }
        Thread.sleep(10000);
        Assert.assertTrue(RegistryManager.instance().isActive());
    }
}
