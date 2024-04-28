package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();

		Seller seller = sellerDao.findById(3);
		System.out.println("==== TEST1: seller findById ====");
		System.out.println(seller);
		
		System.out.println("==== TEST2: seller findAll ====");
		List<Seller> sel = sellerDao.findAll();
		
		for(Seller s : sel) {
			System.out.println(s);
		}


	}
}
