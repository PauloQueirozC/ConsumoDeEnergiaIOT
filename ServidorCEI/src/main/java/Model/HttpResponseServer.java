/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 * Classe que representa a resposta do servidor HTTP.
 */
public class HttpResponseServer {
    /** Sequência de caracteres que representa a quebra de linha. */
    final String CRLF = "\n\r";
    
    /** Versão do protocolo HTTP. */
    private String version;
    
     /** Corpo da resposta em formato JSON. */
    private String json;
    
    /** Estado da resposta HTTP. */
    private String state;
    
    /**
     * Construtor da classe com um json.
     *
     * @param version Versão do protocolo HTTP.
     * @param json Corpo da resposta em formato JSON.
     * @param state Estado da resposta HTTP.
     */
    public HttpResponseServer(String version, String json, String state){
        this.version = version;
        this.json = json;
        this.state = state;
    }
    
    /**
     * Construtor da classe sem um json.
     *
     * @param version Versão do protocolo HTTP.
     * @param state Estado da resposta HTTP.
     */
    public HttpResponseServer(String version, String state){
        this.version = version;
        this.json = null;
        this.state = state;
    }
    
    /**
     * Obtém a resposta HTTP em formato de String.
     *
     * @return String que representa a resposta HTTP.
     */
    public String getResposeString(){
        if (this.json == null)
            return (this.version + " " + this.state + CRLF);
        else
            return (this.version + " " + this.state + CRLF +
                    "Content-Length: " + this.json.getBytes().length + CRLF +
                    CRLF +
                    this.json + 
                    CRLF + CRLF);
    }
}
