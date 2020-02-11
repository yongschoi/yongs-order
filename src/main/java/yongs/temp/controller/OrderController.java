package yongs.temp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import yongs.temp.model.Order;
import yongs.temp.service.OrderService;

@RestController
@RequestMapping("/stock")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);	

    @Autowired
    OrderService service;
    
    @GetMapping("/code/{code}")
    public Order getStock(@PathVariable("code") String code) throws Exception{
    	logger.debug("yongs-stock|StockController|getStock()");    	
        return service.getStock(code);
    }
}