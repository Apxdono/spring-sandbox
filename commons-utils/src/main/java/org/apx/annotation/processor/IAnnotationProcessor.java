package org.apx.annotation.processor;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 28.03.14
 * Time: 15:45
 * To change this template use File | Settings | File Templates.
 */
public interface IAnnotationProcessor<K> extends Serializable {

    <E extends IAnnotationProcessor<K>> E setTarget(Object target);
    K processAnnotation();

}
