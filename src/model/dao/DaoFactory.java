package model.dao;

import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	// Esse método retorna o tipo da interface, mas internamente ela vai retornar o
	// tipo da implementação
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC();
	}
}
