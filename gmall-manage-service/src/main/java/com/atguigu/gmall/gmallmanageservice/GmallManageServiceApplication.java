package com.atguigu.gmall.gmallmanageservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@EnableDubbo
@SpringBootApplication
@MapperScan("com.atguigu.gmall.gmallmanageservice.mapper")
@ComponentScan("com.atguigu.gmall")
public class GmallManageServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallManageServiceApplication.class, args);
	}

}
