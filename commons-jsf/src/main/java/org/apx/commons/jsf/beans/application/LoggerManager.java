package org.apx.commons.jsf.beans.application;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apx.utils.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 04.04.14
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
@ApplicationScoped
@ManagedBean
public class LoggerManager implements Serializable {
    private static final long serialVersionUID = 4517931087018216609L;

    List levels;
    List loggers;

    @PostConstruct
    public void init(){
        loggers = new ArrayList();
        levels = new ArrayList();
        levels.add(Level.OFF);
        levels.add(Level.FATAL);
        levels.add(Level.ERROR);
        levels.add(Level.WARN);
        levels.add(Level.INFO);
        levels.add(Level.DEBUG);
        levels.add(Level.ALL);
    }


    public List getLoggers(){


        if(loggers.isEmpty()){
            Set set = new HashSet();

            Enumeration v = LogManager.getCurrentLoggers();

            while (v.hasMoreElements()) {
                Object next =  v.nextElement();
                set.add(ReflectionUtils.getFieldValue(next,"parent"));
            }
                loggers.addAll(set);
        }
        return loggers;
    }


    public List<Level> getLevels(){
        return levels;
    }
}
