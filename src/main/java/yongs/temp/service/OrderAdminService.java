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
public class OrderAdminService {
	// status 0:주문/결제완료, 1:상품준비, 2:배송중, 3:배송완료 
	private static final Logger logger = LoggerFactory.getLogger(OrderAdminService.class);
	
	@Autowired
    OrderRepository repo;

	// 1:상품준비
	public void prepare(String no) {
		logger.debug("yongs-order|OrderAdminService|prepareProduct({})", no);
		Order entity = repo.findByNo(no);
		entity.setStatus(1);
		repo.save(entity);
	}
	
	// 0:주문/결제완료 건수 조회
	public List<Order> findOnPayment() {
		return repo.findOnStatus(0);
	}	

	// 1:상품준비 건수 조회
	public List<Order> findOnPrepare() {
		return repo.findOnStatus(1);
	}	
	
	// 3: 기간별 배송완료 건수 조회
	public List<Order> findOnPeriod(int period) {		
		logger.debug("yongs-order|OrderAdminService|findOnPeriod({})", period);
		long curr = System.currentTimeMillis();
		long periodMillis = curr-TimeUnit.DAYS.toMillis(period);
		return repo.findOnPeriod(periodMillis, Sort.by(Sort.Direction.DESC, "opentime"));
	}
}
