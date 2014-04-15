package org.apx.remote;

import org.apx.interaction.ProxyUtils;
import org.apx.remote.services.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14.03.14
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
@ContextConfiguration("classpath:interaction.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestRMI {

    @Test
    public void testRMI() throws RemoteException, NotBoundException, BrokenBarrierException, InterruptedException {
        String name = "HelloService";

        int threadCount = 10000;
        Registry r = LocateRegistry.getRegistry("localhost",1199);
        List<Runnable> list = new ArrayList<Runnable>(threadCount);

        final CyclicBarrier gate = new CyclicBarrier(threadCount);

        for (int i = 0; i < threadCount ; i++) {
            final int finalI = i;
            list.add(new Runnable() {
                @Override
                public void run() {
                    int num = finalI+1;
                    try {
                        gate.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                    HelloService serv = null;
                    try {
                        serv = ProxyUtils.lookup(HelloService.class);
                        String helloText = serv.sayHello();
                        int cnt = serv.countUsers();
                        String report = "Thread# '%d': Hello message: '%s', User count: %d.";
                        System.out.println(String.format(report,num,helloText,cnt));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        for (int i = 0; i < threadCount ; i++) {
            Thread t = new Thread(list.get(i));
            t.start();
        }

        Thread.sleep(20000);
    }
}
