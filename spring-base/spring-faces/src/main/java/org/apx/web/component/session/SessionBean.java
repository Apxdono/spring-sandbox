package org.apx.web.component.session;

import org.apx.repo.CommonRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 12.03.14
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
@Component
//@ManagedBean
//@SessionScoped
//@ViewScoped
@Named
@Scope("view")
public class SessionBean implements Serializable {

    static Logger LOG = LoggerFactory.getLogger("spring.test");

    @Inject
    CommonRepo repo;

    boolean renderHidden;

    @PostConstruct
    public void init(){
        LOG.info("Bean: {}, DB connected? {}",toString(),repo.checkConnection());
        renderHidden = false;
    }

    public String getGreeting(){
        return "Hello from view bean.";
    }

    public void tryMe(AjaxBehaviorEvent event) {
        renderHidden = !renderHidden;
    }


    public boolean isRenderHidden() {
        return renderHidden;
    }

    public void setRenderHidden(boolean renderHidden) {
        this.renderHidden = renderHidden;
    }
}
