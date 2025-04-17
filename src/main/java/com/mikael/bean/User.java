package com.mikael.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author
 * @version 1.0
 * @date 2025/4/15
 */

public class User implements Serializable {
    private final static long serialVersionUID = 1L;

    private String id;
    private String name;
    private String APP_id;
    private Integer numbers;
    private String address;

    public User() {
    }

    public User(String id, String name, String APP_id, Integer numbers, String address) {
        this.id = id;
        this.name = name;
        this.APP_id = APP_id;
        this.numbers = numbers;
        this.address = address;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAPP_id() {
        return APP_id;
    }

    public void setAPP_id(String APP_id) {
        this.APP_id = APP_id;
    }

    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", APP_id='" + APP_id + '\'' +
                ", numbers=" + numbers +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(APP_id, user.APP_id) && Objects.equals(numbers, user.numbers) && Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, APP_id, numbers, address);
    }
}
