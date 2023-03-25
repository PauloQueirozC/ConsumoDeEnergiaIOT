
package Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class main {

    /**
     * Este método é responsável por iniciar o servidor e aguardar a conexão de clientes.
     * Ele cria um ServerSocket na porta 54322 e aguarda solicitações de conexão de clientes.
     * Para cada cliente conectado, uma nova thread é criada para lidar com as solicitações desse cliente.
     * @param args argumentos passados por linha de comando
     * @throws IOException se houver um erro ao abrir ou manipular o socket do servidor
     */
    public static void main(String[] args) throws IOException {
        //1 - Definir o serverSocket (abrir porta de conexão)
        ServerSocket servidorSocket = new ServerSocket(54322);
        System.out.println("A porta 54322 foi aberta!");
        System.out.println("Servidor esperando receber mensagens de clientes...");
        
        while (true) {
            //2 - Aguardar solicitações de conexão de clientes 
            Socket socket = servidorSocket.accept();
            //Mostrar endereço IP do cliente conectado
            System.out.println("Cliente " + socket.getInetAddress().getHostAddress() + " conectado");
            
            //3 - Definir uma thread para cada cliente conectado
            ThreadServer thread = new ThreadServer(socket);
            thread.start();
        }
    }
}
