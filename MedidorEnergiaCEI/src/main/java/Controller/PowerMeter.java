/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.*;
import json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author paulo
 */
public class PowerMeter {

    private String ip;
    private int port;
    private String id;
    private String proprietary;
    private float kwh; // Consumo total
    private float kwhPerSecond;  // Variação do consumo
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private DataSendingThread thread;
    
    /**
     * Construtor da Classe PowerMeter que ja tenta conectar o mesmo ao servidor.
     * 
     * @param proprietary nome do proprietario do medidor.
     * @param ip ip do servidor oa qual o medidor irá se conectar.
     * @param port porta que o servidor esta ouvindo.
     */ 
    public PowerMeter(String proprietary, String ip, int port) throws IOException {
        this.proprietary = proprietary;
        this.kwh = 0;
        this.kwhPerSecond = 0;
        this.ip = ip;
        this.port = port;
        this.id = this.conect(ip, port);
    }

    /**
     * Metodo para retornar o id do medidor
     * 
     * @return ip do medidor
     */
    public String getId() {
        return id;
    }

    /**
     * Metodo para retornar o consumo em kwh do medidor
     * 
     * @return consumo em kwh do medidor
     */
    public float getKwh() {
        return kwh;
    }
    
    /**
     * Metodo para retornar o aumento do consumo por segundo do medidor
     * 
     * @return crescimento do consumo do medidor por segundo
     */
    public float getKwhPerSecond() {
        return kwhPerSecond;
    }
    /**
     * Metodo para modificar o aumento do consumo por segundo do medidor
     * 
     * @param kwhPerSecond novo crescimento do consumo do medidor por segundo
     */
    public void setKwhPerSecond(float kwhPerSecond) {
        this.kwhPerSecond = kwhPerSecond;
    }
    
    /**
     * Estabelece uma conexão de socket com o endereço IP e número de porta especificados.
     * Envia uma solicitação JSON para o servidor com as informações proprietário e data/hora atual.
     * Fecha a conexão de socket após receber uma resposta do servidor.
     *
     * @param ip o endereço IP para se conectar
     * @param port o número de porta para se conectar
     * @return o ID da resposta JSON recebida do servidor
     * @throws IOException se ocorrer um erro de E/S ao estabelecer a conexão de socket
     */
    private String conect(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        String json = "{\"proprietary\":\"" + this.proprietary + "\",\"date\":\"" + formattedDate + "\"}";
        HttpRequestClient httpRequest = new HttpRequestClient("POST", "/conect", json);

        HttpResponseClient httpResponse = this.sendReceiveHttp(socket, httpRequest.toString());
        this.closeAll();
        return httpResponse.getJson().getString("id");
    }
    
    /**
     * Recebe uma resposta do servidor por meio do socket especificado.
     * Lê os bytes recebidos do servidor e os converte em uma string.
     *
     * @param socket o socket para receber a resposta do servidor
     * @return a resposta do servidor como uma string
     * @throws IOException se ocorrer um erro de E/S ao ler os bytes recebidos do servidor
     */
    private String receiveResponse(Socket socket) throws IOException {
        this.is = socket.getInputStream();
        StringBuilder strBuilder = new StringBuilder();
        int _byte;
        while (is.available() <= 0);
        while (is.available() > 0) {
            _byte = is.read();
            if (_byte != 13) {
                strBuilder.append((char) _byte);
            }
        }
        return strBuilder.toString();
    }
    
    /**
     * Envia uma solicitação para o servidor por meio do socket especificado.
     * Escreve a string da solicitação no stream de saída do socket.
     *
     * @param socket o socket para enviar a solicitação
     * @param requestStr a string da solicitação a ser enviada
     * @throws IOException se ocorrer um erro de E/S ao escrever a string da solicitação no stream de saída do socket
     */
    private void sendRequest(Socket socket, String requestStr) throws IOException {
        this.os = socket.getOutputStream();
        os.write(requestStr.getBytes());
    }
    
    /**
     * Envia uma solicitação para o servidor por meio do socket especificado
     * e retorna a resposta do servidor como um objeto HttpResponseClient.
     * 
     * @param socket o socket para enviar a solicitação
     * @param requestStr a string da solicitação a ser enviada
     * @return a resposta do servidor como um objeto HttpResponseClient
     * @throws IOException se ocorrer um erro de E/S ao enviar ou receber a solicitação
     */
    private HttpResponseClient sendReceiveHttp(Socket socket, String requestStr) throws IOException {
        this.sendRequest(socket, requestStr);
        return new HttpResponseClient(this.receiveResponse(socket));
    }
    
    /**
     * Fecha todos os recursos associados a este objeto, incluindo o socket,
     * o stream de entrada e o stream de saída.
     *
     * @throws IOException se ocorrer um erro de E/S ao fechar o socket, o stream de entrada ou o stream de saída
     */
    private void closeAll() throws IOException{
        this.os.close();
        this.is.close();
        this.socket.close();
    }
    
    /**
     * Incrementa o valor do consumo por segundo ao valor total do consumo n vezes
     * 
     * @param n quantas vezes vai ocorrer o incremento
     */
    public void increaseConsumption(int n){
        this.kwh = this.kwh + this.kwhPerSecond * n;
    }
    
    /**
     * Inicia a thread que aumenta o consumo e envia o novo consumo para o servidor  
     */
    public void startIncreaseConsumption(){
        this.thread = new DataSendingThread(this);
        this.thread.start();
        System.out.print("Medidor iniciado com suscesso");
    }
    
    /**
     * Envia uma atualização de consumo para o servidor por meio do socket.
     * 
     * @throws IOException se ocorrer um erro de E/S ao enviar ou receber a solicitação
     */
    public void sendConsumption() throws IOException{
        this.socket = new Socket(this.ip, this.port);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = now.format(formatter);

        String json = "{\"idPowerMeter\":\"" + this.id +
                    "\",\"date\":\"" + formattedDate + 
                    "\",\"kwh\":"+ this.kwh +"}";
        HttpRequestClient httpRequest = new HttpRequestClient("POST", "/newMensure", json);

        HttpResponseClient httpResponse = this.sendReceiveHttp(socket, httpRequest.toString());
        this.closeAll();
    }
}
