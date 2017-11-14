/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servidor;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Usuario9
 */
public class Tablero extends JPanel implements ActionListener{
    private Timer timer; 
    private ArrayList<Circulo> circulo;
    private Carro personajePrincipal;
    private  ServerSocket serverSocket ;
    private Socket cliente ;
    
    private int puntaje = 0;
    public Tablero(){
        try {
            this.personajePrincipal = new Carro(100,200);
            this.serverSocket = new ServerSocket(8000);
            this.circulo = new ArrayList<Circulo>();
            this.circulo.add(new Circulo(20,20));
            this.circulo.add(new Circulo(100,80));
            this.circulo.add(new Circulo(80,120));
            
            cliente=this.serverSocket.accept();
            System.out.println("Acepte un cliente");
            Thread proceso = new Thread(new Hilo(cliente,personajePrincipal));
            proceso.start();
            this.timer = new Timer(50, this);
            this.timer.start();
        } catch (IOException ex) {
            Logger.getLogger(Tablero.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    protected void paintComponent(Graphics g) {
         super.paintComponent(g);
         for(Circulo c: this.circulo)
            c.dibujar(g,this);
         
         this.personajePrincipal.dibujar(g,this);
         g.drawString("Puntaje " + puntaje, 40, 40);
         
         
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        validarColisiones();
         for(Circulo c: this.circulo)
            c.mover();
            repaint();
        
    }
     
    
    public void validarColisiones(){
        Rectangle recPersonaje= this.personajePrincipal.obtenerRectangulo();
        ArrayList<Circulo> copia = (ArrayList<Circulo>) this.circulo.clone();
        for(Circulo c : circulo){
           Rectangle RecCir = c.obtenerRectangulo();
           if(recPersonaje.intersects(RecCir)){
               copia.remove(c);
               this.puntaje++;
           }
           this.circulo=copia;   
           
        }
    }

}
