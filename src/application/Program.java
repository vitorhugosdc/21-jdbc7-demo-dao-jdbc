package application;

import java.time.format.DateTimeFormatter;
import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		Department dpt = new Department(1, "Books");

		Seller seller = new Seller(21, "Bob", "bob@gmail.com", new Date(), 3000.0, dpt);
		
		System.out.println(seller);

	}
}
