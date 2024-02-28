## Atividade do módulo 10 do curso de Java Avançado da Mentorama

Criar uma API utilizando Spring WebFlux para armazenar lista de filmes.
- A API deve conter endpoints de CRUD para cadastro e leitura de avaliação de filmes por usuário, armazenando os seguintes campos:
  - Título do Filme (único por usuário);
    - Ou seja, cada usuário pode avaliar somente 1 vez cada filme. Uma avaliação substituiria a outra do mesmo filme.
  - Nota (de 1 a 5);
  - Comentário;
  - Id do usuário.
- A API não deve diretamente realizar a atualização no banco de dados, e sim enviar uma mensagem para uma fila onde no processamento da mensagem a avaliação será persistida.
- O Service vai enviar uma mensagem para que um Listener (Receiver) receba essa mensagem e salve no banco de dados.
- Não precisa retornar a entidade persistida via HTTP. Pode retornar o status 200, ou seja, o método pode ser void. O importante é que na consulta o valor esteja gravado no BD.
- O Listener das mensagens deve receber a mensagem e persistir a nova avaliação utilizando o broker que desejar (ActiveMQ, RabbitMQ, Kafka etc.)
