package jpabook.jpashop;

import org.junit.jupiter.api.Test;

public class HelloTest {

    @Test
    public void helloTest() {
        Hello hello = new Hello();
        hello.setData("Hello Spring Test");
        String data = hello.getData();
        System.out.println("data = " + data);
    }
}
