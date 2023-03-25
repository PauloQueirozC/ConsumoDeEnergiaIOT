/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Measure;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author paulo
 */
public class teste {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        Calendar c = Calendar.getInstance();
        Date data = c.getTime();
	DateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	Date data2 = f.parse("1999-01-01 00:00:00");
	System.out.println(data2);

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");//HH:mm:ss");
	System.out.println("Data formatada: "+sdf.format(data));

	//Converte Objetos
	System.out.println("Data convertida: "+sdf.parse("02/08/1970"));
        
        System.out.println("-------------------");
        String[] vetor = {"taco", "gato", "cabra", "queijo", "pizza"};
        System.out.println(vetor.toString());
        
        System.out.println("-------------------");
        double numero = 123.14159265359;
        DecimalFormat df = new DecimalFormat("#.##");
        String numeroFormatado = df.format(numero);
        System.out.println(numeroFormatado);
        
        System.out.println("-------------------");
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);
        System.out.println("Data e hora formatadas: " + formattedDate);
        
        System.out.println("-------------------");
        ArrayList<Measure> teste = new ArrayList<Measure>();
        Measure measure = new Measure(100, data);
        teste.add(measure);
        measure.setValueKwh(200);
        System.out.println(measure.toString());
        System.out.println(teste.get(0).toString());
    }
}
