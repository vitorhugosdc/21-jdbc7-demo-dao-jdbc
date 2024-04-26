package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import db.DB;
import db.DbException;
import db.DbIntegrityException;

public class Program {

	public static void main(String[] args) {

		/*
		 * // Cria a conexão com o banco de dados 
		 * 
		 * Connection conn = DB.getConnection();
		 * 
		 * // PreparedStatement: Serve para montar um comando SQL para ser executado 
		 * 
		 *  Geralmente é usado para comandos SQLs que vão inserir/alterar/deletar dados
		 *  Pois é um objeto que permite montar uma consulta SQL
		 *  deixando os parâmetros para serem colocados depois com o chamado placeholder  '?'
		 *  Obviamente pode-se usar só o Statement, mas ai ele não suporta o placeholder,
		 *  teria que usar por exemplo "UPDATE seller SET BaseSalary = BaseSalary " + salaryToIncrease...
		 * 
		 * // ResultSet: Representa um objeto contendo o resultado da consulta em
		 * formado de tabela, contendo linhas (registros) e colunas (campos)
		 * 
		 * ResultSet rs = null;
		 */

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");

		Connection conn = null;

		PreparedStatement st = null;

		ResultSet rs = null;

		try {

			// Cria a conexão com o banco de dados
			conn = DB.getConnection();

			//Necessário para transformar a transação em atômica, ou seja, 
			//só realiza a transação com confirmação explicita do programador através do conn.commit()
			conn.setAutoCommit(false);
			
			// PreparedStatement: Objeto que permite montar uma consulta SQL
			// deixando os parâmetros para serem colocados depois

			// Instância um statement e armazena em st do tipo Statement
			st = conn.prepareStatement("DELETE FROM department " // A operação UPDATE não retorna uma tabela, 
														//nem nada como de inserção que retorna os IDs gerados
														//ou a consulta que possui a tabela gerada com os resultados da consulta
										+ "WHERE "
										+ "Id = ?"); // ? serve como placeholder, ou seja,
																					// um lugar onde depois eu vou
																					// colocar o valor como no caso
																					// serão inseridos 2 valores na
																					// tabela, coloca 2 placeholders ?

			// Detalhe: o ? pode ser usando em outro lugar também, como ao alterar salary =
			// salary + ? no PreparedStatement, ai na hora de atualizar salario por exemplo
			// coloca a variável desejada

			// O segundo argumento do prepareStatement, Statement.RETURN_GENERATED_KEYS serve só pra INSERÇÃO, então foi removido já que não foi gerado Ids novos, apenas atualizado um já existente
			st.setInt(1, 5);

			int rowsAffected = st.executeUpdate(); // Para alterar os dados de um PreparedStatement chama o
													// .executeUpdate();
													// Esse método retorna o número de linhas alteradas (rowsAffected)

			conn.commit(); // Confirma a transação com atômicidade, ou seja, ou todos do lote são
			// executados com sucesso, ou nenhum é (se der erro vai pro catch do
			// SQLException onde faz o rollback)
			
			if (rowsAffected > 0) {
				System.out.println("Done! Rows affected: " + rowsAffected);

			} else {
				System.out.println("No rows affected!");
			}

		} catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
				//Aqui é caso tenha dado ruim na transação e efetuou o rollback
			} catch (SQLException e1) {
				throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
				//Aqui já é caso deu ruim até no rollback
			}
		} finally {
			// rs.close(); //esse é a maneira original, mas como tem que ficar dando
			// try/catch, usar do nélio no DB.closeResultSet(rs);
			DB.closeResultSet(rs);
			// Mesma coisa do de cima
			DB.closeStatement(st);
			DB.closeConnection(); // SEMPRE FECHAR A CONEXÃO POR ÚLTIMO
		}
	}
}
