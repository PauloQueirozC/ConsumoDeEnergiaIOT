/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import json.JSONObject;

/**
 * A classe HttpRequestClient representa uma mensagem HTTP que é enviada para um servidor através de um socket.
 * A mensagem contém um cabeçalho e um corpo no formato JSON.
 */
public class HttpRequestClient {
    private String method; // método HTTP (ex: GET, POST, etc.)
    private String path; // caminho da URL
    private String version; // versão do protocolo HTTP
    private String json; // corpo da mensagem em formato JSON
    
    /**
     * Cria uma nova mensagem HTTP com um corpo em formato JSON.
     * 
     * @param method o método HTTP da mensagem
     * @param path o caminho da URL da mensagem
     * @param json o corpo da mensagem em formato JSON
     */
    public HttpRequestClient(String method, String path, String json) {
        this.method = method;
        this.path = path;
        this.version = "HTTP/1.1";
        this.json = json;
    }
    
    /**
     * Cria uma nova mensagem HTTP com um corpo em formato JSON.
     * 
     * @param method o método HTTP da mensagem (ex: GET, POST, etc.)
     * @param path o caminho da URL da mensagem
     * @param json o corpo da mensagem em formato JSONObject
     */
    public HttpRequestClient(String method, String path, JSONObject json) {
        this.method = method;
        this.path = path;
        this.version = "HTTP/1.1";
        this.json = json.toString();
    }
    
    /**
    * Retorna uma string que representa uma mensagem HTTP com um cabeçalho Content-Length e um corpo em JSON.
    *
    * @return Uma string que representa uma mensagem HTTP com um cabeçalho Content-Length e um corpo em JSON.
    */
    @Override
    public String toString(){
        String CRLF = "\n\r";
        String string = ( this.method + " " + this.path + " " + this.version + CRLF
                        + "Content-Length: " + this.json.getBytes().length + CRLF
                        + CRLF
                        + json);
        return string;
    }
}
