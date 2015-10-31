package main;

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
import banco.ConnectionManager;
import gui.cTelaLogView;
import java.awt.Component;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author luizfelipe
 */
public class cMain {

  private static ConnectionManager banco;
  private static Component frame;
  
  public static void main(String[] args) {

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch(Exception e) {
      e.getStackTrace();
    }

    banco = new ConnectionManager();
    
    banco.conectar();

    cRegistros r = banco.registros();

    if(pergunta() == 0) {

        mostraRegistrosConsole(r);

    } else {

      cTelaLogView t = new cTelaLogView(r);
      t.setVisible(true);

    }

    banco.desconectar();
  }

  private static int pergunta() {
    Object[] o = {"No Console",
                  "No dialogo"};
    return JOptionPane.showOptionDialog(frame,
                                        "Visualizar os registros (logView):",
                                        "LogView",
                                        JOptionPane.YES_NO_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null, //do not use a custom Icon
                                        o, //the titles of buttons
                                        o[0]); //default button title
  }

  private static void mostraRegistrosConsole(cRegistros r) {


    System.out.println();

    System.out.println("----------------------------------------------------------------------------------------------");
    System.out.println(" Hora do registro\t\tRegistro");
    System.out.println("----------------------------------------------------------------------------------------------");


    Collections.sort(r.l);

    
    for(int i = 0; i < r.l.size(); i++) {
      Integer c = r.l.get(i);
      System.out.println(r.lst.get(c).hora + "\t" + r.lst.get(c).registro );
    }
//    for(int i = 0; i < r.tamanho(); i++) {
//
//      System.out.println(r.hora(i) + "\t" + r.registo(i));
//
//    }

    System.out.println();
    System.out.println("----------------------------------------------------------------------------------------------");
    System.out.println();

  }
}
