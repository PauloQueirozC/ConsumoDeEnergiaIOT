/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe que representa uma medida de consumo de energia elétrica.
 */
public class Measure {
    /** Valor em kWh da medida. */
    private float valueKwh;
    
    /** Data e hora da medida. */
    private Date date;

    /**
     * Define o valor em kWh da medida.
     *
     * @param valueKwh Valor em kWh da medida.
     */
    public void setValueKwh(float valueKwh) {
        this.valueKwh = valueKwh;
    }
    
    /**
     * Construtor da classe.
     *
     * @param valueKwh Valor em kWh da medida.
     * @param date Data e hora da medida.
     */
    public Measure(float valueKwh, Date date) {
        this.valueKwh = valueKwh;
        this.date = date;
    }
    
    /**
     * Obtém a data e hora da medida.
     *
     * @return Objeto Date que representa a data e hora da medida.
     */
    public Date getDate(){
        return this.date;
    }
    
    /**
     * Obtém o valor em kWh da medida.
     *
     * @return Valor em kWh da medida.
     */
    public float getValueKwh() {
        return this.valueKwh;
    }
    
    /**
     * Retorna a medida em formato String no padrão JSON.
     *
     * @return String que representa a medida em formato JSON.
     */
    @Override
    public String toString(){
        DateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return ("{"
                + "\"date\":\"" + sd.format(this.date) + "\","
                + "\"valueKwh\":" + this.valueKwh
                + "}");
    }
}
