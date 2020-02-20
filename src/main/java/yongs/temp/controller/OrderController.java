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
import yongs.temp.service.OrderAdminService;
import yongs.temp.service.OrderEventService;
import yongs.temp.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);	

    @Autowired
    OrderService service;
    @Autowired
    OrderAdminService adminService;
    @Autowired
    OrderEventService eventService;
    
    @PostMapping("/create") 
    public void create(@RequestBody Order order) throws Exception{
    	logger.debug("yongs-order|OrderController|create({})", order);
    	eventService.create(order);
    }
    
    @GetMapping("/statusAll/{email}") 
    public List<Order> findOnStatusAllByUser(@PathVariable("email") String email) throws Exception{
    	logger.debug("yongs-order|OrderController|findOnStatusAllByUser({})", email);
        return service.findOnStatusAllByUser(email);
    }
    @GetMapping("/onDelivery/{email}") 
    public List<Order> findOnDeliveryByUser(@PathVariable("email") String email) throws Exception{
    	logger.debug("yongs-order|OrderController|findOnDeliveryByUser({})", email);
        return service.findOnDeliveryByUser(email);
    }
 
    @PutMapping("/complete/{no}") 
    public void complete(@PathVariable String no) throws Exception{
    	logger.debug("yongs-order|OrderController|complete({})", no);
    	service.complete(no);
    } 
    
    @GetMapping("/onPeriod/{email}/{period}") 
    public List<Order> findOnPeriodByUser(@PathVariable("email") String email, @PathVariable("period") int period) throws Exception{
    	logger.debug("yongs-order|OrderController|findOnPeriodByUser({}, {})", email, period);
        return service.findOnPeriodByUser(period, email);
    }
    
    @GetMapping("/admin/onPayment") 
    public List<Order> findOnPayment() throws Exception{
    	logger.debug("yongs-order|OrderController|findOnPayment()");
        return adminService.findOnPayment();
    }

    @GetMapping("/admin/onPrepare") 
    public List<Order> findOnPrepare() throws Exception{
    	logger.debug("yongs-order|OrderController|findOnPrepare()");
        return adminService.findOnPrepare();
    }
 
    @GetMapping("/admin/onPeriod/{period}") 
    public List<Order> findOnPeriod(@PathVariable("period") int period) throws Exception{
    	logger.debug("yongs-order|OrderController|findOnPeriod({})", period);
        return adminService.findOnPeriod(period);
    }
    
    @PutMapping("/admin/prepare/{no}") 
    public void prepare(@PathVariable String no) throws Exception{
    	logger.debug("yongs-order|OrderController|prepare({})", no);
    	adminService.prepare(no);
    } 
}