package org.apx.utils.tests;

import org.apx.utils.ReflectionUtils;
import org.apx.utils.tests.model.SimpleModel;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 26.02.14
 * Time: 13:08
 * To change this template use File | Settings | File Templates.
 */
public class FieldsTest {


    /**
     * Branch coverage for ReflectionUtils.field methods
     */
    @Test
    public void testGetField(){
        SimpleModel model = new SimpleModel();

        Field f1 = ReflectionUtils.field(model.getClass(),"name");
        Field f2 = ReflectionUtils.field(model.getClass(),"name",String.class);
        Assert.assertEquals(f1, f2);

        Field f3 = ReflectionUtils.field(model.getClass(),"name",Double.class);
        Field f4 = ReflectionUtils.field(model.getClass(),"",Double.class);
        Assert.assertNull(f3);
        Assert.assertNull(f4);
        f4 = ReflectionUtils.field(null,"sum",Double.class);
        Assert.assertNull(f4);

        Field f5 = ReflectionUtils.field(model.getClass(),"values", List.class);
        Field f6 = ReflectionUtils.field(model.getClass(),"values");
        Assert.assertEquals(f5, f6);

        Field f7 = ReflectionUtils.field(model.getClass(),"values2", List.class);
        Field f8 = ReflectionUtils.field(model.getClass(),"values2", Collection.class);
        Assert.assertEquals(f7, f8);

        Field f9 = ReflectionUtils.field(model.getClass(),"values2", List.class);
        Field f10 = ReflectionUtils.field(model.getClass(),"values2", Collection.class,Object.class);
        Assert.assertEquals(f9, f10);

    }
}
