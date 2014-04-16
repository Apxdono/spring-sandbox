package org.apx.remote;

import com.google.common.base.Stopwatch;
import org.apx.interaction.ProxyUtils;
import org.apx.interaction.RegistryManager;
import org.apx.interaction.enums.RemoteMessage;
import org.apx.interaction.enums.RemoteResponse;
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
import java.util.concurrent.TimeUnit;

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

        int threadCount = 100;
        List<Thread> list = new ArrayList<Thread>(threadCount);

        final CyclicBarrier gate = new CyclicBarrier(threadCount);

        for (int i = 0; i < threadCount ; i++) {
            list.add(new Thread(new Runner(gate,i)));
            list.get(i).start();
        }
        Stopwatch timer = new Stopwatch().start();

        for (int i = 0; i < threadCount ; i++) {
            list.get(i).join();
        }

        timer.stop();

        System.out.println("All threads executed. Time: "+timer.elapsed(TimeUnit.MILLISECONDS));
    }


    private class Runner implements Runnable{

        CyclicBarrier gate;
        int number = 0;

        public Runner(CyclicBarrier g, int num){
            gate = g;
            number=num;
        }

        @Override
        public void run() {
            try {
                gate.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            RemoteResponse<HelloService> resp = RegistryManager.instance().proxy(HelloService.class);
            while (!resp.getMessage().equals(RemoteMessage.OK)){
                try {
                    System.out.println("waiting");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }finally {
                    resp = RegistryManager.instance().proxy(HelloService.class);
                }
            }
            if(resp.getMessage().equals(RemoteMessage.OK)){
                try {
                    String hello = resp.getResult().sayHello();
                    int cnt = resp.getResult().countUsers();
                    String msg = "Thread#%d: Hi: %s , Users:%d";
                    System.out.println(String.format(msg,number,hello,cnt));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("ERROR");
            }
        }
    }
}
