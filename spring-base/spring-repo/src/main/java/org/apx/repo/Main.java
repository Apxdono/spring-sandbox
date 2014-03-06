package org.apx.repo;

import org.apx.helper.AutowireHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 06.03.14
 * Time: 18:11
 * To change this template use File | Settings | File Templates.
 */
//@Component
public class Main {

    @Autowired
    CommonRepo repo;

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("db.xml");
        Main m = new Main();
        AutowireHelper.autowire(m);
        System.out.println("Count: "+m.count());
    }

    public Object count(){
        return repo.count();
    }

}
