/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import json.JSONObject;

/**
 *
 * @author paulo
 */
public class HttpRequestServer {
    private String method;
    private String path;
    private String version;
    private JSONObject json;
    
    /**
     * Cria um novo objeto HttpRequestServer com base em uma solicitação HTTP.
     *
     * @param request a string que contém a solicitação HTTP
     */
    public HttpRequestServer(String request){
        String[] splitRequest = request.split("\n");
        
        String[] Header1 = splitRequest[0].split(" ");

        this.method = Header1[0];
        this.path = Header1[1];
        this.version = Header1[2];
        this.json = new JSONObject(splitRequest[splitRequest.length-1]);
    }
    
    /**
     * Retorna o método HTTP usado na solicitação.
     *
     * @return o método HTTP usado na solicitação
     */
    public String getMethod() {
        return method;
    }

    /**
     * Retorna o caminho da solicitação HTTP.
     *
     * @return o caminho da solicitação HTTP
     */
    public String getPath() {
        return path;
    }

    /**
     * Retorna a versão do protocolo HTTP usada na solicitação.
     *
     * @return a versão do protocolo HTTP usada na solicitação
     */
    public String getVersion() {
        return version;
    }

    /**
     * Retorna o objeto JSON contido na solicitação HTTP.
     *
     * @return o objeto JSON contido na solicitação HTTP
     */
    public JSONObject getJson() {
        return this.json;
    }
    
    
    /**
     * Retorna uma String que mostra tudo que o objeto leu da requesição.
     *
     * @return a String que mostra tudo que o objeto leu da requesição
     */
    @Override
    public String toString(){
        return (this.method  + " | " +
                this.path    + " | " +
                this.version + " | " +
                this.json.toString());
    }
}
