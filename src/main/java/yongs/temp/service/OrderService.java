package yongs.temp.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import yongs.temp.dao.OrderRepository;
import yongs.temp.model.Order;

@Service
public class OrderService {
	// status 0:주문/결제완료, 1:상품준비, 2:배송중, 3:배송완료 
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
 
	@Autowired
    OrderRepository repo;

	// 주문상태(0:주문/결제완료, 1:상품준비, 2:배송중) 조회 by user
	public List<Order> findOnStatusAllByUser(String email) {
		logger.debug("yongs-order|OrderService|findOnStatusAllByUser({})", email);
		return repo.findOnStatusAllByUser(2, email);
	}  
	        
	// 2:배송중 건수 조회 by user
	public List<Order> findOnDeliveryByUser(String email) {
		logger.debug("yongs-order|OrderService|findOnDeliveryByUser({})", email);
		return repo.findOnStatusByUser(2, email);
	} 
	 
	// 3: 기간별 배송완료 건수 조회 by user
	public List<Order> findOnPeriodByUser(int period, String email) {		
		logger.debug("yongs-order|OrderService|findOnPeriodByUser({}, {})", period, email);
		long curr = System.currentTimeMillis();
		long periodMillis = curr-TimeUnit.DAYS.toMillis(period);
		return repo.findOnPeriodByUser(periodMillis, email, Sort.by(Sort.Direction.DESC, "opentime"));
	}
	
	// 3:배송완료
	public void complete(String no) {
		logger.debug("yongs-order|OrderService|complete({})", no);
		Order entity = repo.findByNo(no);
		entity.setStatus(3);
		repo.save(entity);
	}
}
