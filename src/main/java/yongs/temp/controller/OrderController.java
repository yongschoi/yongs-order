package yongs.temp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    	logger.debug("yongs-order|OrderController|create({})", order);
        service.create(order);
    }
    
    @PutMapping("/user") 
    public List<Order> findByStatusAndUser(@RequestBody Order order) throws Exception{
    	logger.debug("yongs-order|OrderController|findByStatusAndUser({})", order);
        return service.findByStatusAndUser(order);
    }
 
    @GetMapping("/status/{status}") 
    public List<Order> findByStatus(@PathVariable("status") int status) throws Exception{
    	logger.debug("yongs-order|OrderController|findByStatus({})", status);
        return service.findByStatus(status);
    }
    
    @PutMapping("/accept/{no}") 
    public void accept(@PathVariable String no) throws Exception{
    	logger.debug("yongs-order|OrderController|accept({})", no);
    	service.accept(no);
    } 
}