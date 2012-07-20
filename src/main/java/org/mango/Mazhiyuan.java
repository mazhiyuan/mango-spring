package org.mango;

/**
 * Created by IntelliJ IDEA.
 * User: mazhiyuan
 * Date: 12-7-16
 * Time: 下午4:57
 * To change this template use File | Settings | File Templates.
 */
public class Mazhiyuan extends Madel{
    private String name;
    private int age;
    /**
     * must have a default constructor
     */
    public Mazhiyuan(){

    }
    public Mazhiyuan(String name, int age) {
        this.name = name;
        this.age = age;
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

}
