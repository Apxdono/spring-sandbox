package org.apx.annotation.processor;

import org.apache.commons.lang3.StringUtils;
import org.apx.annotation.ViewPage;
import org.apx.utils.ReflectionUtils;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 28.03.14
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public class ViewPageProcessor implements IAnnotationProcessor<String> {

    Object obj;

    @Override
    public <E extends IAnnotationProcessor<String>> E setTarget(Object target) {
        obj = target;
        return (E) this;
    }

    @Override
    public String processAnnotation() {
        if(obj == null) return "";

        ViewPage vp = ReflectionUtils.getClassAnnotation(obj.getClass(), ViewPage.class, false);
        if(vp == null || StringUtils.isBlank(vp.url())){
            Object id = ReflectionUtils.getFieldValue(obj,"id");
            return "view.jsf?id="+id;
        }  else {
            Object fieldValue = ReflectionUtils.getFieldValue(obj,vp.field());
            return String.format(vp.url(),fieldValue);
        }
    }
}
