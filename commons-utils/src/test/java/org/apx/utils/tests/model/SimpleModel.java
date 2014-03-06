package org.apx.utils.tests.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 26.02.14
 * Time: 13:08
 * To change this template use File | Settings | File Templates.
 */
public class SimpleModel {

    public String name;
    int age;
    private Double sum;
    protected List<Integer> values;
    protected ArrayList<Integer> values2;


    public SimpleModel() {
        name = "Test";
        age = 10;
        sum = 200.5;
        values = Arrays.asList(new Integer[]{1,2,3,4,5});
    }

    public SimpleModel(String name, int age, Double sum, List<Integer> values) {
        this.name = name;
        this.age = age;
        this.sum = sum;
        this.values = values;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }
}
