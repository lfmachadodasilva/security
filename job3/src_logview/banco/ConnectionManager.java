/*
 * PUC-Rio - Pontificia Universidade Catolica
 * Bacharelado em Sistemas de Informacao
 *
 * INF 1416 - Seguranca da Informacao 2010.1     Turma: 3WA
 * Trabalho P1
 * Professor: Anderson Oliveira da Silva
 *
 * Grupo: Henrique Taunay
 *        Luiz Felipe Machado
 */
package banco;

import java.sql.*;
import main.cRegistros;

public class ConnectionManager {
  //atributes

  private static Connection con;
  private static Statement stm;
  private static ResultSet rs;

  public ConnectionManager() {
    //empty
  }
  //methods

  public void conectar() {
    try {

      /* Tenta se conectar ao Driver */
      Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
      
    } catch(ClassNotFoundException e) {

      System.out.println("Impossivel carregar o Driver.");
      System.exit(0);

    }

    try {

      /* nomedobanco eh o nome que voce deu anteriormente ao seu alias */
      con = DriverManager.getConnection("jdbc:odbc:banco");
      stm = con.createStatement();

    } catch(SQLException sqle) {

      System.out.println("Problema ao conectar o banco de dados! " + sqle.getMessage());
      System.exit(0);

    }

    //System.out.println("Banco de dados conectado.");
  }

  public void desconectar() {

    try {

      con.close();

    } catch(SQLException sqle) {

      System.out.println("Problema ao desconectar!");
      System.exit(0);
      
    }

    //System.out.println("Banco de dados desconectado.");
    
  }

  public cRegistros registros() {

    cRegistros r = new cRegistros();

    try {
      rs = stm.executeQuery("SELECT * FROM tabelaRegistros");
      // achou um usuario com o mesmo login
      while(rs.next()) {

        r.add(rs.getTimestamp("dataHora"), rs.getString("codigo"), rs.getString("usuario"), rs.getInt("rid"));
        
      }

    } catch(SQLException e) {

      System.out.println("Erro ao acessar banco de dados: " + e.getMessage());

    } finally {
      try {

        rs.close();

      } catch(Exception e) {

        System.out.println("Erro ao fechar o resultset!!!" + e.getMessage());
        
      }
    }

    for(int i = 0; i < r.l.size(); i++) {
      r.alteraRegisto(r.l.get(i), buscaMensagem(Integer.valueOf(r.lst.get(r.l.get(i)).getRegistro())));
    }

    return r;
  }

  public String buscaMensagem(int c) {

    String s = null;
    
    try {
      rs = stm.executeQuery("SELECT * FROM tabelaMsgs WHERE codigo=" + c);
      // achou um usuario com o mesmo login
      if(rs.next()) {

        s = rs.getString("mensagem");

      }

    } catch(SQLException e) {

      System.out.println("Erro ao acessar banco de dados: " + e.getMessage());

    } finally {
      try {

        rs.close();

      } catch(Exception e) {

        System.out.println("Erro ao fechar o resultset!!!" + e.getMessage());

      }
    }

    return s;
  }
}
