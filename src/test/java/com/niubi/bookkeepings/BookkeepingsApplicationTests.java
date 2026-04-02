package com.niubi.bookkeepings;

import org.junit.jupiter.api.Test;


class BookkeepingsApplicationTests {
    public static class Phone{
        private String brand;
        private int price;
        private String color;
        public  void call(){
            System.out.println("价值"+price+"元"+color+"的"+brand+"手机正在打电话...");
        }
        public  void sendMessage(){
            System.out.println("价值"+price+"元"+color+"的"+brand+"手机正在发短信...");


        }

        public Phone(String brand, String color, int price) {
            this.brand = brand;
            this.color = color;
            this.price = price;
        }

        public Phone() {
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }
    }

    @Test
    void PhoneTest() {
        Phone XiaoMiPhone=new Phone("XiaoMi","黑色",3999);
        Phone HuaWeiPhone=new Phone("HuaWei","白色",3999);
        XiaoMiPhone.call();
        HuaWeiPhone.sendMessage();
    }

}
