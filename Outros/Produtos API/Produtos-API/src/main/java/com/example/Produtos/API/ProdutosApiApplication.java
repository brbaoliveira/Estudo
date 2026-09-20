package com.example.Produtos.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProdutosApiApplication {

    @GetMapping("/hello-world")
    public String helloWorld(){
        return "Hello World!";
    }

	public static void main(String[] args) {
		SpringApplication.run(ProdutosApiApplication.class, args);
	}

}
 