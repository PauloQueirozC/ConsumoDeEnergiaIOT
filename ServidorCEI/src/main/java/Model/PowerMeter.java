/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


/**
 * A classe PowerMeter representa um medidor de energia elétrica.
 * 
 * Ele mantém o histórico de medidas e calcula o consumo mensal e se está em alerta.
 * 
 * Cada instância de PowerMeter possui um ID exclusivo, um nome do proprietário e um histórico de medidas.
 * 
 * O histórico de medidas é representado como um ArrayList de objetos Measure, 
 * cada um contendo um valor de kWh medido e a data em que foi feito.
 */
public class PowerMeter {
    /**
     * O nome do proprietário deste medidor de energia.
     */
    private String proprietary;
    
    /**
     * O ID exclusivo deste medidor de energia.
     */
    private String id;
    
    /**
     * O histórico de medidas deste medidor de energia.
     */
    private ArrayList<Measure> measurementHistoric;
    
    /**
     * A constante de retorno de carro e avanço de linha.
     */
    final String CRLF = "\n\r";

    /**
     * Cria um novo medidor de energia com um ID, nome do proprietário e uma medida inicial.
     * 
     * @param id           O ID exclusivo deste medidor de energia.
     * @param proprietary  O nome do proprietário deste medidor de energia.
     * @param date         A data da medida inicial no formato yyyy-MM-dd HH:mm:ss.
     * 
     * @throws ParseException Se houver um erro ao analisar a data.
     */
    public PowerMeter(int id, String proprietary, String date) throws ParseException {
        this.id = String.valueOf(id);
        this.proprietary = proprietary;
        this.measurementHistoric = new ArrayList<Measure>();
        this.newMensure(date, 0);
    }

    /**
     * Retorna o ID exclusivo deste medidor de energia.
     * 
     * @return O ID exclusivo deste medidor de energia.
     */
    public String getId() {
        return id;
    }
    
    /**
     * Retorna o nome do proprietário deste medidor de energia.
     * 
     * @return O nome do proprietário deste medidor de energia.
     */
    public String getProprietary() {
        return this.proprietary;
    }
    
    /**
     * Adiciona uma nova medida ao histórico de medidas deste medidor de energia.
     * 
     * @param dateString  A data da nova medida no formato yyyy-MM-dd HH:mm:ss.
     * @param kwh         O valor da nova medida em quilowatts-hora.
     * 
     * @throws ParseException Se houver um erro ao analisar a data.
     */
    public void newMensure(String dateString, float kwh) throws ParseException{
        DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = f.parse(dateString);
        this.measurementHistoric.add(new Measure(kwh, date));
    }
    
    /**
     * Retorna um array de medidas do histórico deste medidor de energia.
     * 
     * @param amountOfMeasurements A quantidade de medidas a serem retornadas.
     * 
     * @return Um array de medidas do histórico deste medidor de energia.
     */
    public Measure[] getHistoric(int amountOfMeasurements){
        int measurementHistorySize = measurementHistoric.size();
        int historicSize = Math.min(amountOfMeasurements, measurementHistorySize);
        
        Measure[] history = new Measure[historicSize];
        
        for (int i = 0; i < historicSize; ++i){
            history[i] = measurementHistoric.get(measurementHistorySize-1-i);
        }
        return history;
    }
    
    /**
     * Retorna a ultima medição desse medidor de energia.
     * 
     * @return a ultima medição desse medidor de energia.
     */
    public float lastMeasure(){
        return this.measurementHistoric.get(this.measurementHistoric.size()-1).getValueKwh();
    }
    
    /**
    * Verifica se há uma anomalia nas medições elétricas.
    *
    * @return true se a última medição de energia for maior que 1000 kWh ou se
    *         a taxa de consumo for maior que 50 kWh/s, caso contrário retorna false.
    */
    public boolean isOnAlert(){
        if (this.measurementHistoric.size() > 1){
            Measure measure1 = this.measurementHistoric.get(this.measurementHistoric.size() - 1);
            Measure measure2 = this.measurementHistoric.get(this.measurementHistoric.size() - 2);
            Date date1 = measure1.getDate();
            Date date2 = measure2.getDate();
           
            long diffMillis = date1.getTime() - date2.getTime();
            double diffSecond = Math.abs(diffMillis) / (1000);
            
            double kw = Math.abs(measure1.getValueKwh() - measure2.getValueKwh()) / diffSecond;
            
            return (measure1.getValueKwh() > 10000.00 || kw > 50.00);
        }
        return false;
    }
    
    /**
    * Retorna a última medição elétrica registrada em um determinado mês e ano.
    *
    * @param month o mês para o qual se deseja recuperar a última medição (1 = Janeiro, 2 = Fevereiro, etc.)
    * @param year o ano para o qual se deseja recuperar a última medição
    * @return a última medição elétrica registrada no mês e ano especificados, ou null se não houver medição registrada
    */
    private Measure lastMeasureOfMonth(int month, int year){
        if (!(1 < month && month < 13))
            return null;
        
        Calendar calendar = Calendar.getInstance();
        
        for (int i = this.measurementHistoric.size() - 1; i >= 0; i--){
            calendar.setTime(this.measurementHistoric.get(i).getDate());
            if (calendar.get(Calendar.YEAR) == year && (calendar.get(Calendar.MONTH) + 1) == month)
                return this.measurementHistoric.get(i);
        }
        return null;
    }
    
    /**
    * Calcula o consumo de energia elétrica em um determinado mês e ano.
    *
    * @param month o mês para o qual se deseja calcular o consumo (1 = Janeiro, 2 = Fevereiro, etc.)
    * @param year o ano para o qual se deseja calcular o consumo
    * @return o consumo de energia elétrica no mês e ano especificados, ou -1 se o mês for inválido, ou -2 se não houver
    *         medição registrada para um dos meses.
    */
    public float consumptionOfMonth(int month, int year){
        if (!(1 < month && month < 13))
            return -1;
        
        Measure measure1, measure2;
        if (month == 1){
            measure1 = this.lastMeasureOfMonth(1, year);
            measure2 = this.lastMeasureOfMonth(12, year-1);
        } else {
            measure1 = this.lastMeasureOfMonth(month, year);
            measure2 = this.lastMeasureOfMonth(month-1, year);
        }
        
        if (measure1 == null)
            return -2;
        else if (measure2 == null)
            return measure1.getValueKwh();
        return measure1.getValueKwh() - measure2.getValueKwh();
    }
    
    /**
    * Retorna uma representação em formato de String do objeto Meter no formato json, 
    * contendo suas propriedades e histórico de medições.
    *
    * @return uma representação em formato de String do objeto Meter
    */
    @Override
    public String toString(){
        return ("{\"id\":\"" + this.id + "\"," +
                "\"proprietary\":\"" + this.proprietary + "\"," +
                 "\"MeasuresHistoric\":" + Arrays.toString(this.getHistoric(3)) + 
                "}");
    }
}
