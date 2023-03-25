/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulo
 */
public class DataSendingThread extends Thread{
    private PowerMeter powerMeter;
    /**
     * Construtor da classe
     * @param pw Medidor de energia que esse classe pegara os dados e enviará 
     *           para o servidor
     */
    public DataSendingThread(PowerMeter pw){
        this.powerMeter = pw;
    }
    
    /**
     * Executa a thread, incrementando o consumo de energia a cada segundo e enviando-o ao servidor.
     */
    @Override
    public void run(){
        while (true){
            try {
                Thread.sleep(5000);
                this.powerMeter.increaseConsumption(5);
                this.powerMeter.sendConsumption();
            } catch (InterruptedException ex) {
                Logger.getLogger(DataSendingThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DataSendingThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
