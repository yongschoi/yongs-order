package yongs.temp.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import yongs.temp.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
	public Order findByCode(final String code);
}