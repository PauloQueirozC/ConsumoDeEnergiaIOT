/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import json.JSONObject;


/**
 *
 * @author paulo
 */
public class ThreadServer extends Thread{
    private static int nextId = 0;
    private static ArrayList<PowerMeter> meters = new ArrayList<PowerMeter>();
    private Socket socket;
    
    public ThreadServer(Socket socket){
        this.socket = socket;
    }
    
    
    /**
     * Este método é responsável por executar a thread para lidar com as solicitações do cliente.
     * Ele define um InputStream para receber os dados do cliente e processa esses dados para determinar a
     * solicitação HTTP feita pelo cliente. Em seguida, executa a ação correspondente e envia uma resposta HTTP
     * de volta ao cliente através de um OutputStream. Finalmente, fecha todos os streams e o socket de comunicação.
     */
    @Override
    public void run(){
        System.out.println(Thread.currentThread().getName());

        try {
            //1 - Definir stream de entrada de dados no servidor
            InputStream entrada = socket.getInputStream();
            StringBuilder strBuilder = new StringBuilder();
            int _byte;
            while (entrada.available() <= 0);
            while (entrada.available() > 0) {
                _byte = entrada.read();
                if (_byte != 13)
                    strBuilder.append((char) _byte);
            }
            //System.out.println("Oq Recebeu:");
            //System.out.println(strBuilder.toString());
            //System.out.println("-----------");
            
            HttpRequestServer httpReq = new HttpRequestServer(strBuilder.toString());
            System.out.println("Oq Recebeu: ");
            System.out.println(httpReq.toString());
            
            //2 - Detectar oq fazer e realizar
            HttpResponseServer httpRes = null;
            if (httpReq.getMethod().contentEquals("POST")){
                //System.out.println("Solicitação HTTP POST");
                if (httpReq.getPath().contentEquals("/conect"))
                    httpRes = this.conectPowerMeter(httpReq);
                
                else if (httpReq.getPath().contentEquals("/newMensure"))
                    httpRes = this.newMensure(httpReq);
                
                else if (httpReq.getPath().contentEquals("/consumption"))
                    httpRes = this.getConsumption(httpReq);
                
                else if (httpReq.getPath().contentEquals("/myPowerMeters"))
                    httpRes = this.IdMeterByProprietary(httpReq);
                
                else if (httpReq.getPath().contentEquals("/historic"))
                    httpRes = this.getHistoric(httpReq);
                
                else if (httpReq.getPath().contentEquals("/electricityBill"))
                    httpRes = this.electricityBill(httpReq);
            }
            
            if (httpRes == null)
                httpRes = new HttpResponseServer(httpReq.getVersion(), "400 Bad Request");
            
            //3 - Definir stream de saída de dados do servidor
            OutputStream saida = socket.getOutputStream();
            saida.write(httpRes.getResposeString().getBytes());

            //4 - Fechar streams de entrada e saída de dados
            entrada.close();
            saida.close();
            
            //5 - Fechar socket de comunicação
            socket.close();
            
        } catch (IOException ioe) {
            System.out.println("Erro: " + ioe.toString());
        } catch (InterruptedException ex) {
            Logger.getLogger(ThreadServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ThreadServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Busca um PowerNeter na lista usando o id informado, retornando o PowerMeter
     * solicitado ou null se ele não existir
     * @param id o ID do objeto PowerMeter a ser retornado
     * @return o objeto PowerMeter correspondente ao ID fornecido, ou null se não houver correspondência
     */
    private PowerMeter getMeterById(String id){
        for (int i = 0; i < this.meters.size(); i++){
            if (this.meters.get(i).getId().equals(id))
                return this.meters.get(i);
        }
        return null;
    }
    
    /**
    * Retorna uma lista de objetos PowerMeter Com o mesmo proprietario enviado
    *
    * @param proprietary a string usada para filtrar os objetos PowerMeter
    * @return uma lista de objetos PowerMeter que correspondem ao filtro de "proprietary"
    */
    private ArrayList<PowerMeter> getMeterByProprietary(String proprietary){
        ArrayList<PowerMeter> proprietaryMeters = new ArrayList<PowerMeter>();
        for (int i = 0; i < this.meters.size(); i++){
            if (meters.get(i).getProprietary().equals(proprietary))
                proprietaryMeters.add(meters.get(i));
        }
        System.out.println("\nO prorpietario "+proprietary+" tem: "+ proprietaryMeters.toString());
        return proprietaryMeters;
    }
    
    /**
    * Retorna uma lista dos Id's dos PowerMeter do mesmo proprietario requisitado.
    *
    * @param proprietary a string usada para filtrar os objetos PowerMeter
    * @return uma lista de IDs de objetos PowerMeter que correspondem ao filtro de "proprietary", ou null se nenhum objeto for encontrado
    */
    private String[] getIdMeterByProprietary(String proprietary){
        ArrayList<PowerMeter> meters = this.getMeterByProprietary(proprietary);
        if (meters.isEmpty())
            return null;
        String[] ids = new String[meters.size()];
        for (int i = 0; i < meters.size(); i++){
            ids[i] = meters.get(i).getId();
        }
        return ids;
    }
    
    /**
    * Conecta um novo medidor de energia e retorna uma resposta HTTP correspondente.
    *
    * @param httpReq o objeto HttpRequestServer contendo os dados da requisição
    * @return um objeto HttpResponseServer com a resposta HTTP correspondente
    * @throws InterruptedException se o thread atual for interrompido enquanto estiver aguardando o recurso sincronizado
    * @throws ParseException se ocorrer um erro ao analisar a string JSON fornecida na requisição
    */
    private synchronized HttpResponseServer conectPowerMeter(HttpRequestServer httpReq) throws InterruptedException, ParseException{
        System.out.println("Conectando novo medidor com id: " + nextId);
        HttpResponseServer httpResp = null;
        if (httpReq.getJson().has("proprietary") && httpReq.getJson().has("date")){
            PowerMeter newPowerMeter = new PowerMeter(nextId,
                                                      httpReq.getJson().getString("proprietary"),
                                                      httpReq.getJson().getString("date"));
            meters.add(newPowerMeter);
            httpResp = new HttpResponseServer(httpReq.getVersion(),
                                        "{\"id\":\"" + newPowerMeter.getId()+ "\"}",
                                        "201 Created");
            nextId += 1;
            System.out.println(newPowerMeter.toString());    
        } else
            httpResp = new HttpResponseServer(httpReq.getVersion(), "422 Unprocessable Entity");
        
        
        return httpResp;
    }
    
    
    /**
    * Cria uma nova leitura de um medidor de energia e retorna uma resposta HTTP correspondente.
    *
    * @param httpReq o objeto HttpRequestServer contendo os dados da requisição
    * @return um objeto HttpResponseServer com a resposta HTTP correspondente
    * @throws ParseException se ocorrer um erro ao analisar a string JSON fornecida na requisição
    */
    private synchronized HttpResponseServer newMensure(HttpRequestServer httpReq) throws ParseException{
       JSONObject json = httpReq.getJson();
       
       if (!(json.has("idPowerMeter") && json.has("date") && json.has("kwh")))
            return new HttpResponseServer(httpReq.getVersion(), "422 Unprocessable Entity"); 
       
       
       for (int i = 0; i < this.meters.size(); i++){
           if (meters.get(i).getId().equals(json.getString("idPowerMeter"))){
               meters.get(i).newMensure(json.getString("date"),  (float) json.getDouble("kwh"));
               System.out.println("Medidor att: " + meters.get(i).toString());
               
               return new HttpResponseServer(httpReq.getVersion(), "201 Created");
           }
       }
       
       return new HttpResponseServer(httpReq.getVersion(), "404 Not Found");
    }
    /**
    * Este método recebe uma solicitação HTTP contendo o proprietária de medidores de energia elétrica
    * e retorna uma resposta HTTP contendo as informações de consumo dos medidores associados a esse proprietario
    * ou a mensagem de erro associado a algum problema no processo.
    * @param httpReq a solicitação HTTP recebida contendo as informações da empresa proprietária
    * @return uma resposta HTTP contendo informações de consumo dos medidores de energia elétrica associados ao proprietario
    * @return uma resposta HTTP com o estato de erro ao executar o metodo
    * @throws NullPointerException se a solicitação HTTP não contiver informações JSON válidas
    */
    private HttpResponseServer getConsumption(HttpRequestServer httpReq){
        JSONObject json = httpReq.getJson();
        if (!json.has("proprietary"))
            return new HttpResponseServer(httpReq.getVersion(), "422 Unprocessable Entity");
        
        ArrayList<PowerMeter> myMeters = this.getMeterByProprietary(json.getString("proprietary"));
        if (myMeters.isEmpty())
            return new HttpResponseServer(httpReq.getVersion(), "404 Not Found");
        
        StringBuilder sb = new StringBuilder();
        PowerMeter tempMeter;
        for(int i = 0; i < myMeters.size(); i++){
            tempMeter = myMeters.get(i);
            if (i > 0)
                sb.append(",");
            sb.append("{"+"\"id\":\""+ tempMeter.getId()+
                       "\",\"consumption\":"+ Float.toString(tempMeter.lastMeasure()) +
                       ",\"alert\":"+ tempMeter.isOnAlert() +"}");
        }
    
        String jsonString = "{\"consumptions\":" + "[" + sb.toString() + "]}";
        
        return new HttpResponseServer(httpReq.getVersion(), jsonString, "200 OK");
    }
    
    /**
    * Este método recebe uma solicitação HTTP contendo o proprietária de medidores de energia elétrica
    * e retorna uma resposta HTTP contendo os IDs dos medidores associados a ele ou uma mensagem de erro
    * caso o proprietario não exista ou o dado enviado esta incorreto.
    * @param httpReq a solicitação HTTP recebida contendo as informações da empresa proprietária
    * @return uma resposta HTTP contendo os IDs dos medidores de energia elétrica associados à empresa especificada
    * @return uma resposta HTTP contendo uma mensgem de erro.
    * @throws NullPointerException se a solicitação HTTP não contiver informações JSON válidas
    */
    private HttpResponseServer IdMeterByProprietary(HttpRequestServer httpReq){
        JSONObject json = httpReq.getJson();
        if (!json.has("proprietary"))
            return new HttpResponseServer(httpReq.getVersion(), "422 Unprocessable Entity");
        
        String[] ids = this.getIdMeterByProprietary(json.getString("proprietary"));
        
        if (ids == null)
            return new HttpResponseServer(httpReq.getVersion(), "404 Not Found");
        
        String jsonString = "{\"ids\":" + Arrays.toString(ids) + "}";
        
        return new HttpResponseServer(httpReq.getVersion(), jsonString, "200 OK");
    }
    
    /**
    * Obtém o histórico de medições de um medidor de energia específico.
    *
    * @param httpReq o objeto HttpRequestServer contendo os dados da requisição HTTP
    * @return um objeto HttpResponseServer contendo a resposta HTTP com o histórico de medições 
    * do medidor de energia ou uma mensagem de erro ao executar o metodo
    */
    private HttpResponseServer getHistoric(HttpRequestServer httpReq){
        JSONObject json = httpReq.getJson();
        if (!json.has("idPowerMeter"))
            return new HttpResponseServer(httpReq.getVersion(), "422 Unprocessable Entity");
        
        PowerMeter findMeter;
        for (int i = 0; i < this.meters.size(); i++){
            findMeter = meters.get(i);
           if (findMeter.getId().equals(json.getString("idPowerMeter"))){
               Measure[] historic = findMeter.getHistoric(20);
               String jsonString = "{\"historic\":" + Arrays.toString(historic) + "}";
               return new HttpResponseServer(httpReq.getVersion(), jsonString, "200 OK");
           }
       }
       
       return new HttpResponseServer(httpReq.getVersion(), "404 Not Found");
    }
    
    /**
    * Calcula o valor da conta de luz de um medidor de energia para um determinado mês e ano.
    *
    * @param httpReq o objeto HttpRequestServer contendo os dados da requisição HTTP
    * @return um objeto HttpResponseServer contendo a resposta HTTP com o valor da conta de luz
    */
    private HttpResponseServer electricityBill(HttpRequestServer httpReq){
        JSONObject json = httpReq.getJson();
        if (!(json.has("idPowerMeter") && json.has("month") && json.has("year")))
            return new HttpResponseServer(httpReq.getVersion(), "422 Unprocessable Entity");
        
        
        PowerMeter pm = this.getMeterById(json.getString("idPowerMeter"));
        if (pm == null){
            System.out.print("Medidor não encontrado");
            return new HttpResponseServer(httpReq.getVersion(), "422 Not Found");
        }
        float consumption = pm.consumptionOfMonth(json.getInt("month"), json.getInt("year"));
        
        if (consumption == -1)
            return new HttpResponseServer(httpReq.getVersion(), "422 Unprocessable Entity");
        else if (consumption == -2)
            return new HttpResponseServer(httpReq.getVersion(), "404 Not Found");
        
        DecimalFormat df = new DecimalFormat("#.##");
        String jsonString = "{\"value\":" + (consumption * 0.5)  + "}";
        return new HttpResponseServer(httpReq.getVersion(), jsonString, "200 OK");
    }
}   
