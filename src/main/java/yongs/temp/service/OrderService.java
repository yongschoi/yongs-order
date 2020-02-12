package yongs.temp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import yongs.temp.dao.OrderRepository;
import yongs.temp.model.Order;

@Service
public class OrderService {
	// for sender
	private static final String ORDER_STOCK_EVT = "order-to-stock";
	// for listener
	private static final String DELIVERY_ORDER_EVT = "delivery-to-order";	
	private static final String ROLLBACK_EVT = "stock-rollback, payment-rollback, delivery-rollback";
	
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

		// Order객체를 Kafka Message로 사용하기 위해 String으로 변환
		ObjectMapper mapper = new ObjectMapper();
		String orderStr = mapper.writeValueAsString(order);

		// to stock
		kafkaTemplate.send(ORDER_STOCK_EVT, orderStr);
	}
	
	@KafkaListener(topics = DELIVERY_ORDER_EVT)
	public void complete(String orderStr, Acknowledgment ack) throws JsonProcessingException {	
		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.readValue(orderStr, Order.class);
		// 모든 프로세스가 완료되었으며, 최종 Order정보를 DB에 저장한다.
		repo.insert(order);	
		// kafka commit을 한다.(성공할때 까지 메시지 계속 수신함. 무한 retry)
		ack.acknowledge();
	}
	
	@KafkaListener(topics = ROLLBACK_EVT)
	public void rollback(String orderStr, Acknowledgment ack) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Order order = mapper.readValue(orderStr, Order.class);
		System.out.println("Order No [" + order.getNo() + "] Rollback !!!");
	}
}
