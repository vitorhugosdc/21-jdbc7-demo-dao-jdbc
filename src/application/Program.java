package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		/*
		 * Utiliza a classe auxiliar DaoFactory pra instânciar o nosso SellerDao. Dao
		 * factory é o objeto responsável por instânciar os nossos DAOs Estamos criando
		 * uma variável do tipo da interface, mas recebendo um SellerDaoJDBC, que possui
		 * a implementação dessa interface
		 */
		SellerDao sellerDao = DaoFactory.createSellerDao();

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");

		System.out.println("==== TEST1: seller findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);

		System.out.println("==== TEST2: seller findAll ====");
		List<Seller> findAll = sellerDao.findAll();

		for (Seller s : findAll) {
			System.out.println(s);
		}

		System.out.println("==== TEST3: seller insert ====");

		try {
			Seller sel2 = new Seller(1, "Jao", "jao@gmail.com", sdf.parse("24/12/1970"), 2000.00,
					new Department(3, "Computers"));
			sellerDao.insert(sel2);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}

		System.out.println("==== TEST4: seller deleteById ====");
		sellerDao.deleteById(3);

		System.out.println("==== TEST5: seller findByDepartment ====");

		Department department = new Department(2, null);
		List<Seller> findByDepartment = sellerDao.findByDepartment(department);

		for (Seller obj : findByDepartment) {
			System.out.println(obj);
		}
		
		System.out.println("==== TEST6: seller update ====");
		Seller sellerUpdate = sellerDao.findById(1);
		sellerUpdate.setName("Martha Waine");
		sellerDao.update(sellerUpdate);
		System.out.println("Update completed");
	}
}
