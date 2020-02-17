package yongs.temp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import yongs.temp.dao.OrderRepository;
import yongs.temp.model.Order;
import yongs.temp.vo.Delivery;
import yongs.temp.vo.User;

@Service
public class OrderService {
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

	// for sender
	private static final String ORDER_STOCK_EVT = "order-to-stock";
	// for listener
	private static final String DELIVERY_ORDER_EVT = "delivery-to-order";
	private static final String DELIVERY_UPDATE_EVT = "deliveryUpdate";
	
	@Autowired
    OrderRepository repo;
	@Autowired
    KafkaTemplate<String, String> kafkaTemplate;

	public void create(Order order) throws JsonProcessingException {
		// 주문정보만 생성해서 kafka로 보내고 실제 data 저장은 마지막에 수행한다.
		long curr = System.currentTimeMillis();
		String orderNo = "ORD" + curr;	
		order.setNo(orderNo);
		order.setOpentime(curr);
		// 0: 결제완료, 1: 상품준비, 2: 배송중, 3: 배송완료 
		order.setStatus(0); 

		// Order객체를 Kafka Message로 사용하기 위해 String으로 변환
		ObjectMapper mapper = new ObjectMapper();
		String orderStr = mapper.writeValueAsString(order);

		// to stock
		kafkaTemplate.send(ORDER_STOCK_EVT, orderStr);
		logger.debug("[ORDER to STOCK] Order No [" + order.getNo() + "]");
	}
	
	@KafkaListener(topics = DELIVERY_ORDER_EVT)
	public void complete(String orderStr, Acknowledgment ack) {	
		ObjectMapper mapper = new ObjectMapper();
		try {
			Order order = mapper.readValue(orderStr, Order.class);
			// 모든 프로세스가 완료되었으며, 최종 Order정보를 DB에 저장한다.
			repo.insert(order);	
			// kafka commit을 한다.(acknowledge 수행될 때 까지 메시지는 kafka에 존재하게 됨)
			// ack.acknowledge()는 rollback을 허용 하지 않고 반드시 처리하겠다는 의미임.
			ack.acknowledge();
			logger.debug("[ORDER Complete] Order No [" + order.getNo() + "]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = {"stock-rollback", "payment-rollback", "delivery-rollback"})
	public void rollback(String orderStr, Acknowledgment ack) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			Order order = mapper.readValue(orderStr, Order.class);
			repo.deleteByNo(order.getNo());
			ack.acknowledge();
			logger.debug("[ORDER Rollback] Order No [" + order.getNo() + "]");		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@KafkaListener(topics = DELIVERY_UPDATE_EVT)
	public void updateDelivery(String deliveryStr, Acknowledgment ack) {	
		ObjectMapper mapper = new ObjectMapper();
		try {
			Delivery delivery = mapper.readValue(deliveryStr, Delivery.class);
			Order savedOrder = repo.findByNo(delivery.getOrderNo());
			savedOrder.setDelivery(delivery);
			// 0: 결제완료, 1: 상품준비, 2: 배송중, 3: 배송완료 
			savedOrder.setStatus(2);
			repo.save(savedOrder);
			ack.acknowledge();			
			logger.debug("[ORDER Update from DELIVERY] Order No [" + savedOrder.getNo() + "]");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Order> findByUser(User user) {
		return repo.findByUser(user);
	}
	
	public void accept(String no) {
		Order entity = repo.findByNo(no);
		// 0: 결제완료, 1: 상품준비, 2: 배송중, 3: 배송완료 
		entity.setStatus(1);
		repo.save(entity);
	}
	
	public List<Order> findByStatus(int status) {
		return repo.findByStatus(status);
	}	

	public List<Order> findByStatusAndUser(Order order) {		
		return repo.findByStatusAndUser(order.getStatus(), order.getUser());
	}
}
