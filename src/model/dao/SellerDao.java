package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;
//Interface que define quais métodos poderão ser utilizados no banco de dados do vendedor (Seller)
public interface SellerDao {
	
	void insert(Seller obj);
	void update(Seller obj);
	void deleteById(Integer id);
	Seller findById(Integer id);
	List<Seller> findAll();
	List<Seller> findByDepartment(Department department);
}
