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

		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");

		System.out.println("==== TEST1: seller findById ====");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("==== TEST2: seller findAll ====");
		List<Seller> sel = sellerDao.findAll();
		
		for(Seller s : sel) {
			System.out.println(s);
		}
		
		System.out.println("==== TEST3: seller insert ====");
	
		try {
			Seller sel2 = new Seller(1, "Jao", "jao@gmail.com", sdf.parse("24/12/1970"), 2000.00, new Department(3, "Computers"));
			sellerDao.insert(sel2);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		
		System.out.println("==== TEST4: seller deleteById ====");
		sellerDao.deleteById(3);
		
		System.out.println("==== TEST5: seller findByDepartment ====");
		
		Department department = new Department(2, null);
		List<Seller> findByDepartment = sellerDao.findByDepartment(department);
		
		for(Seller obj: findByDepartment) {
			System.out.println(obj);
		}
	}
}
