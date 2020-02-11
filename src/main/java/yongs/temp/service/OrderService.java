package yongs.temp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import yongs.temp.dao.OrderRepository;
import yongs.temp.model.Order;

@Service
public class OrderService {
	@Autowired
    OrderRepository repo;
	
	public Order getStock(String code) {
		return repo.findByCode(code);
	}
	
	public boolean deductQty(Order stock) {
		Order entity = repo.findByCode(stock.getCode());
		int newQty = entity.getQty() - stock.getQty();
		if(newQty < 0) {
			// 재고 부족
			return false;
		} else {
			entity.setQty(newQty);
			repo.save(entity);
			return true;
		}
	}
}
