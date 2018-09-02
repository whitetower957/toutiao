package com.nowcoder.model;

import org.springframework.stereotype.Component;

//表示当前用户是谁
//使用spring依赖注入
//当前有个对象HostHolder,这个对象是用来存储用户的，通过set的方式存下来
//通过get的方式取得
@Component
public class HostHolder {
    private static ThreadLocal<User> users= new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }
}
