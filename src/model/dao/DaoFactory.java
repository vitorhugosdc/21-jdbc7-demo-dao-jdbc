package model.dao;

import db.DB;
import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;

//Dao factory é o objeto responsável por instânciar os nossos DAOs
public class DaoFactory {

	/*
	 * Esse método retorna o tipo da interface, mas internamente ela vai retornar o
	 * tipo da implementação
	 */
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection()); /*
														 * Como precisamos da conexão na implementação do SellerDaoJDBC,
														 * enviamos ela através do Factory (injeção de dependência, onde
														 * a classe não é responsável por instânciar suas dependências,
														 * estamos enviando ela como parâmetro na instanciação
														 */
	}
	
	public static DepartmentDao createDepartmentDao() {
		return new DepartmentDaoJDBC(DB.getConnection());
	}
}
