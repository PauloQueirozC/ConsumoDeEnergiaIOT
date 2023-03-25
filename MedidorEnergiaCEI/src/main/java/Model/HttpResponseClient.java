/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import json.JSONObject;

/**
 * Classe que representa a resposta do servidor HTTP.
 */
public class HttpResponseClient {
    /** Versão do protocolo HTTP. */
    private String version;
    
    /** Corpo da resposta em formato JSON. */
    private JSONObject json;
    
    /** Estado da resposta HTTP. */
    private String state;
    
    /**
     * Construtor da classe.
     *
     * @param request String que representa a requisição HTTP.
     */
    public HttpResponseClient(String request){
        String[] splitRequest = request.split("\n");
        String[] header1 = splitRequest[0].split(" ");
        
        this.version = header1[0];
        this.state = header1[1];
        
        if (splitRequest.length < 3)
            this.json = null;
        else
            this.json = new JSONObject(splitRequest[splitRequest.length-1]);   
    }
    
    /**
     * Obtém o corpo da resposta em formato JSON.
     *
     * @return Objeto JSONObject que representa o corpo da resposta.
     */
    public JSONObject getJson() {
        return this.json;
    }

    /**
     * Obtém o estado da resposta HTTP.
     *
     * @return Sequência de caracteres que representa o estado da resposta HTTP.
     */
    public String getState() {
        return this.state;
    }
    
    /**
     * Retorna a resposta HTTP em formato de String.
     *
     * @return String que representa a resposta HTTP.
     */
    @Override
    public String toString(){
        String CRLF = "\n\r"; 
        if (this.json == null)
            return (this.version + " " + this.state + CRLF);
        else
            return (this.version + " " + this.state + CRLF +
                    "Content-Length: " + this.json.toString().getBytes().length + CRLF +
                    CRLF +
                    this.json + 
                    CRLF + CRLF);
    }
}
