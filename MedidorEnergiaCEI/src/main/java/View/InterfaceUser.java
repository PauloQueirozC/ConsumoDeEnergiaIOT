/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.PowerMeter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Classe que representa a interface do usuário para o medidor de energia elétrica.
 */
public class InterfaceUser {
    
    /**
     * Método principal que é executado ao iniciar a aplicação. Ele solicita ao usuário as informações necessárias para a conexão com o servidor e cria um objeto PowerMeter. 
     * Em seguida, inicia a contagem de consumo do medidor e exibe o menu de opções para o usuário interagir.
     * 
     * @param args argumentos da linha de comando (não utilizado)
     * @throws IOException se ocorrer um erro de entrada e saída ao criar ou fechar o socket de conexão.
     * @throws InterruptedException se a thread for interrompida enquanto estiver esperando a limpeza da tela.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        PowerMeter powerMeter;

        
        System.out.println("Ip do servidor: ");
        String ip = scanner.nextLine();
        
        System.out.println("Porta: ");
        int port = scanner.nextInt();
        scanner.nextLine();
        
        System.out.println("Dono do medidor: ");
        String proprietary = scanner.nextLine();
        System.out.println("Criando medidor");
        powerMeter = new PowerMeter(proprietary, ip, port);
        clear();
        System.out.println("Iniciando medidor");
        powerMeter.startIncreaseConsumption();
        clear();
        System.out.println("Iniciando Menu");
        String state = "home";
        String response;
        while (!state.equals("exit")){
            switch (state){
                case "home":
                    clear();
                    System.out.println("Medidor: " + powerMeter.getId());
                    System.out.println("Consumo por segundo: " + powerMeter.getKwhPerSecond());
                    System.out.println("\nMenu:");
                    System.out.println("1 - Mudar Consumo");
                    System.out.println("2 - Ver Consumo atual");
                    System.out.println("Outra - Finalizar medidor");
                    
                    response = scanner.nextLine();
                    if (response.equals("1"))
                        state = "mudarConsumo";
                    else if (response.equals("2"))
                        state = "verConsumo";
                    else
                        state = "exit";
                    clear();    
                    break;
                case "mudarConsumo":
                    System.out.println("Medidor: " + powerMeter.getId());
                    System.out.println("Consumo por segundo atual: " + powerMeter.getKwhPerSecond());
                    System.out.println("Novo consumo: ");
                    
                    response = scanner.nextLine();
                    try {
                        float newKwhPerSecond = Float.parseFloat(response);
                        powerMeter.setKwhPerSecond(newKwhPerSecond);
                        state = "home";
                    } catch (NumberFormatException e) {
                        System.out.println("\nValor invalido");
                    }
                    break;
                case "verConsumo":
                    System.out.println("Medidor: " + powerMeter.getId());
                    System.out.println("Consumo: " + powerMeter.getKwh() + " kwh");
                    System.out.println("Precione enter para voltar");
                    response = scanner.nextLine();
                    state = "home";
                    break;
            }
        }
        
        
    }
    /**
     * Metodo para limpar o terminal independente do sistema operacional
     * 
     * @throws IOException
     * @throws InterruptedException 
     */
    private static void clear() throws IOException, InterruptedException{
        // Para Windows
        if (System.getProperty("os.name").contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        // Para Linux e macOS
        else {
            Runtime.getRuntime().exec("clear");
        }
    }    
}
