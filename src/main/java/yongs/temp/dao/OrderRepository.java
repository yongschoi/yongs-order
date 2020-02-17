package yongs.temp.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import yongs.temp.model.Order;
import yongs.temp.vo.User;

public interface OrderRepository extends MongoRepository<Order, String> {
	public Order findByNo(final String no);
	public void deleteByNo(final String no);
	public List<Order> findByUser(User user);
	@Query("{status:{$lte:?0}}")
	public List<Order> findByStatus(int status);
	@Query("{$and:[{status:{$lte:?0}}, {user:?1}]}")
	public List<Order> findByStatusAndUser(int status, User user);
}