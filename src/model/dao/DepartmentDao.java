package model.dao;

import java.util.List;

import model.entities.Department;
//Interface que define quais métodos poderão ser utilizados no banco de dados do departamento (Department)
public interface DepartmentDao {

	void insert(Department obj);
	void update(Department obj);
	void deleteById(Integer id);
	Department findById(Integer id);
	List<Department> findAll();
}
