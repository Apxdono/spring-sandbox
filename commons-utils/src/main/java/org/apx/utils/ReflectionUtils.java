package org.apx.utils;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * All reflection operations are handled in here. Mainly for POJO's
 */
public abstract class ReflectionUtils {

    static Logger LOG = LoggerFactory.getLogger(ReflectionUtils.class);


    public static Field field(Class target, String name) {
        return field(target,name,null,null);
    }

    public static Field field(Class target, String name, Class type) {
        return field(target,name,type,null);
    }

    public static Field field(Class target, String name, Class type, Class stopClass) {

        if(target == null){
            LOG.warn("Target class is null.");
            return null;
        }

        if(StringUtils.isBlank(name)){
            LOG.warn("Target field name is empty.");
            return null;
        }

        if(stopClass == null){
            stopClass = Object.class;
        }

        Field result = null;

        while (!target.equals(stopClass)){
            Field[]  fields = target.getDeclaredFields();
            if(fields != null){
                for (Field f : fields) {
                    if(f.getName().equals(name)){
                        if(type !=null && !ClassUtils.isAssignable(f.getType(),type,true)){
                            continue;
                        }
                        result = f;
                        break;
                    }
                }
            }
            target = target.getSuperclass();
        }

        if(result == null){
            LOG.warn("No field with name '{}' and type '{}' was found.",name,type);
        }

        return result;
    }








}
