package yongs.temp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yongs.temp.model.Order;
import yongs.temp.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);	

    @Autowired
    OrderService service;
    
    @PostMapping("/create") 
    public void create(@RequestBody Order order) throws Exception{
    	logger.debug("yongs-order|OrderController|create()");    	
        service.create(order);
    }
}