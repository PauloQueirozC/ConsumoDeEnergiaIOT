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
        <li><a href="#HTTP">Requesições HTTP</a></li>
        <li><a href="#Conclusao">Conclusão</a></li>
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


<div id="HTTP">
    <h1>Requesições HTTP</h1>
    <p>HTTP (Hypertext Transfer Protocol) é um protocolo de comunicação utilizado para transferir dados pela internet. Ele permite que os clientes, como navegadores web ou aplicativos móveis, possam fazer requisições de recursos em servidores web e receber as respostas correspondentes. No nosso projeto, esse protocolo foi usado entre os nossos clientes e o servidor afim de padronizar todas as requesições podendo criar um servidor mais simplese poder fazer testes via ferramentas de testes de API. A seguir cada requesição existente e a sua funcionalidade:</p>
    <ol>
        <li>POST /conect</li>
        <p>Esta requesição e utilizada apenas pelo medidor onde ele envia via json quem é o seu proprietario e quando ele foi criado. Caso tudo ocorra bem ele recebe um json como resposta contendo qual o ip dele no servidor para ele se indentificar.</p>
        <li>POST /newMensure</li>
        <p>Esta requesição e utilizada apenas pelo medidor para enviar uma nova atualização de seu consumo em khw juntamente com a data dessa medição</p>
        <li>POST /consumption</li>
        <p>Esta requesição e utilizada apenas pelo usuario para pedir o consumo de seus medidores recebendo o seu id, consumo atual e se esse medidor tem algum alerta de consumo excessivo.</p>
        <li>POST /myPowerMeters</li>
        <p>Esta requesição e utilizada apenas pelo usuario para pedir os ids dos seus medidores</p>
        <li>POST /historic</li>
        <p>Esta requesição e utilizada apenas pelo usuario para resgatar o historico das ultiamas 20 atualizações de um de seus medidores</p>
        <li>POST /electricityBill</li>
        <p>Esta requesição e utilizada apenas pelo usuario para gerar a fatura do mes atual calculando o seu gasto do mes pegando o valor atual do medidor e subitraindo o valor da ultima medição do mes passado</p>
    </ol>
</div>

<div id="Conclusao">
    <h1>Conclusão</h1>
    <p>O protótipo cumpre todas as funcionalidades propostas tanto pela parte dos medidores, que enviam constantemente suas medidas e podem ser visualizadas diretamente pelo terminal, quanto pela parte do usuário, que pode solicitar qualquer informação dos seus respectivos medidores, incluindo a fatura do mês. Além disso, suporta uma quantidade limitada de solicitações de clientes para o servidor, uma vez que se trata apenas de um protótipo e não está direcionado para uma grande quantidade de clientes. No entanto, é capaz de suportar uma quantidade razoável para simulação, oque e o seu proposito.</p>
    <p>Apesar de atender a todos os requisitos, o protótipo ainda possui algumas limitações e ações pouco eficientes. Por exemplo, a parte do usuário é feita apenas para testes em ferramentas de API, portanto, não apresenta um terminal ou interface interativa, embora todas as solicitações sejam atendidas. Além disso, a eficiência é afetada pelos medidores que, alem de usarem protocolos http para mandar seus dados fazendo o mesmo ser mais demorado do que o nescessario, mesmo que em troca de um servidor mais simples; ficam sempre criando e destruindo o socket criado com o servidor a cada requesição.</p>
    <p>Concluindo, o prototipo segue bem o seu proposito de prototipar o sistema, pois aplica de forma simples o que deve ser o sistema como um todo mostrando posntos a serem fixado e pontos a serem melhorados afim de atingir um sistema final de monitoramento de consumo energetico nas cidades.</p>
</div>
