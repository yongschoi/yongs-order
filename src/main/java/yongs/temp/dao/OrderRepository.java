package yongs.temp.dao;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import yongs.temp.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
	public Order findByNo(final String no);
	public void deleteByNo(final String no);
	
	@Query("{status:{$eq:?0}}")
	public List<Order> findOnStatus(int status);
	
	@Query("{$and:[{status:{$eq:3}}, {opentime:{$gte:?0}}]}")
	public List<Order> findOnPeriod(long periodMillis, Sort sort);
	
	@Query("{$and:[{status:{$eq:?0}}, {'user.email':?1}]}")
	public List<Order> findOnStatusByUser(int status, String email);
	
	@Query("{$and:[{status:{$eq:3}}, {opentime:{$gte:?0}}, {'user.email':?1}]}")
	public List<Order> findOnPeriodByUser(long periodMillis, String email, Sort sort);	
	
	@Query("{$and:[{status:{$lte:?0}}, {'user.email':?1}]}")
	public List<Order> findOnStatusAllByUser(int status, String email);
	
}