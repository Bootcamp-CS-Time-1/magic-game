# Magic Game
> Projeto do Bootcamp CS

Projeto desenvolvido para o Bootcamp da *Concrete Solutions* que consiste em uma aplicação *mobile* para as plataformas **_Android e iOs_**. Este repositório refere-se ao módulo **Android**. 

Joga Magic? Que tal então ter todas as edições lançadas na palma da mão e ainda montar seu próprio _deck_? É exatamente isso que esse aplicativo irá lhe proporcionar. Com uma lista de todas as cartas lançadas até o momento e a possibilidade de favoritá-las, você poderá ficar por dentro de tudo que rolou e rola no mundo _Magic_ explorando todo o seu acervo.

### Requisitos do projeto

- As cartas devem ser trazidas da _API_ [https://api.magicthegathering.io] e mostradas para o usuário em um _grid_ de 3 colunas
- O usuário poderá pesquisar por uma determinada carta
- É necessário que as cartas estejam agrupadas por :
  1. **Edição**, e posteriormente deve-se fazer o agrupamento por   
  2. **Tipo (categoria)**
 - Ao clicar em uma carta, uma tela de detalhes é aberta com a carta selecionada e um _carousel_ com as demais cartas já carregadas
 - Deve ser possível adicionar uma carta ao _deck_ de favoritos 
 - Precisa-se que o usuário possa visualizar e remover suas cartas favoritas
 
 ### Como rodar
 
 Para rodar o projeto e poder modificar o código fonte é necessário apenas que se tenha uma _IDE_ que suporte desenvolvimento Android instalada no computador. o Android Studio foi o _software_ utilizado no desenvolvimento do projeto e atualmente encontra-se na versão 3.5.
 
 Requisitos do celular Android para rodar a aplicação: 
 - Android a partir da API 21 (Android Lollipop - Api 21)
 
Os testes foram realizados num aparelho na versão 9 do Android (Pie) de Api 29.
 

### Libs

A lista de bibliotecas utilizadas para o desenvolvimento foram: 

- _Glide_ para o carregamento e cache de imagens online.
- _Shimmer_ para proporcionar o efeito de carregamento durante as requisições.
- _Koin_ para injeção de dependências.
- _Mockk_ para criação de mocks.
- _Coroutines_ para implementação das coroutines.
- _Retrofit_ para gerenciar as requisições HTTP.
- _Espresso_ para realização de testes unitários e instrumentados.
- _Material Design_ para implementar o design recomendado pelo Google.

### Funcionalidades já implementadas 

- [x] Listar as cartas
- [x] Agrupar por Edição e Tipos
- [x] Detalhamento de uma única carta
- [ ] Pesquisar por carta
- [ ] favoritar carta
- [ ] retirar da lista de favoritos
- [ ] listar favoritos

### Imagens 

![loading](https://user-images.githubusercontent.com/37271614/65352655-8ea72300-dbc1-11e9-862e-b65d71307dde.jpg)
![list](https://user-images.githubusercontent.com/37271614/65352547-4a1b8780-dbc1-11e9-996a-1d82fa36820c.jpg)
![detail](https://user-images.githubusercontent.com/37271614/65352569-54d61c80-dbc1-11e9-879b-92814aa44e2a.jpg)

