package main;


import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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

/**
 *
 * @author luizfelipe
 */

public class cRegistros {

  public Map<Integer, cData> lst;
  public List<Integer> l;

  public cRegistros() {

    lst = new HashMap<Integer, cData>();
    l = new LinkedList<Integer>();

  }

  public void limpa() {

    lst.clear();
    
  }

  public void add(Timestamp t, String r, String u, Integer i) {

   cData c = new cData();
   c.setHora(t);
   c.setUsuario(u);
   c.setRegistro(r);

   l.add(i);
   lst.put(i, c);
  }


  public void alteraRegisto(int i, String r) {

    cData c = lst.get(i);
    String s = c.getRegistro();
    String u = c.getUsuario();

    if(u.isEmpty() && !r.contains("$")) {

      c.setRegistro(r);
      lst.put(i, c);

    } else {

      s = r.replace("$", u);
      c.setRegistro(s);
      lst.put(i, c);

    }

  }
  
}
