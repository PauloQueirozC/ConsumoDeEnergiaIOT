# Consumo De Energia IOT
<p>
Um protótipo implementado para tornar as cidades mais inteligentes com a IoT é um sistema que automatiza o cálculo do consumo de energia das casas por meio de redes. Esse sistema utiliza dispositivos IoT instalados nas casas, que coletam dados sobre o consumo de energia e os transmitem para um servidor central. O servidor usa algoritmos para analisar os dados e calcular o consumo de energia de cada casa em tempo real. Com essas informações, o sistema pode ajudar as cidades a gerenciar melhor a demanda de energia, reduzir custos e promover um uso mais eficiente da energia. Além disso, esse sistema pode ajudar os consumidores a monitorar e reduzir seu consumo de energia, ajudando a criar uma cidade mais sustentável e inteligente.
</p>

<h1>Sumário</h1>
    <ol>
        <li><a href="#Socket">Socket</a></li> 
        <ol>
            <li><a href="#SocketNoServidor">Socket no Servidor</a></li> 
            <li><a href="#SocketNoCliente">Socket no Cliente</a></li> 
        </ol>
    </ol>
    
<div id="Socket">
    <h1>Socket</h1>
    <p>Socket é um mecanismo de comunicação entre processos em rede. Ele permite que programas em diferentes dispositivos se comuniquem e troquem informações através de uma conexão de rede. Essa conexão é estabelecida através de um endereço IP e um número de porta, que permitem que os dados sejam transmitidos de forma confiável e segura. No projeto esse mecanismo foi utilizado usando classes do pacote java.net mais especificamente a classe Socket que cria um objeto que representa essa comunicaçao entre processos em rede. Tanto para o servidor quanto para o cliente a utilização dessa classe se difere um pouco tanto na sua instancia quanto na utilização de outras classes do mesmo pacote.</p>
</div>

<div id="SocketNoServidor">
    <h2>Socket no Servidor</h2>    
    <p>Em programação de rede, é comum que um servidor fique aguardando por uma solicitação de um cliente para poder executar determinada tarefa. Nesse modelo, o servidor fica em execução, esperando por conexões de clientes, e, quando uma conexão é estabelecida, o servidor processa a solicitação e envia uma resposta, se necessário.</p>
    <p>Em Java, o pacote java.net fornece a classe ServerSocket, que é usada para criar um servidor que aguarda conexões de clientes. Para criar um ServerSocket em Java, é necessário informar a porta em que o servidor irá aguardar as conexões. Uma vez criado o ServerSocket, é possível aguardar por conexões de clientes através do método accept(), que bloqueia a execução do servidor até que uma conexão seja estabelecida.</p>
    <p>Quando uma conexão é estabelecida, um novo objeto Socket é criado para representar a conexão entre o cliente e o servidor. Esse objeto Socket é passado para uma nova thread que irá processar a conexão. O servidor então pode continuar aguardando por novas conexões de clientes, criando novas threads para lidar com cada uma delas. Cada thread é responsável por processar a solicitação do cliente e enviar uma resposta.</p>
</div>

<div id="SocketNoCliente">
    <h2>Socket no Cliente</h2> 
    <p>Comparativamente, a utilização do Socket no cliente é mais simples. No protótipo criado, há dois clientes distintos:</p>
    <p>O primeiro cliente, o usuário, tem acesso a informações sobre o seu consumo e utiliza ferramentas de testes de API, como o Insomnia ou Postman, para se comunicar com o servidor. Essas ferramentas permitem realizar testes automatizados das funcionalidades da API, como verificação de requisições, respostas e tempos de resposta, sem a necessidade de implementação completa do sistema.</p>
    <p>Já o segundo cliente, o medidor, é criado em Java e é responsável por enviar atualizações de consumo em kWh para o servidor. Para isso, ele utiliza a classe Socket para estabelecer uma conexão direta com o servidor, fornecendo o endereço IP e a porta para a comunicação. Com essa técnica, é possível enviar dados em tempo real, evitando atrasos e perda de informações.</p>
</div>
