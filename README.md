README — Smart Solution Maintenance

📱 Smart Solution Maintenance

O Smart Solution Maintenance é um aplicativo mobile desenvolvido para auxiliar instituições filantrópicas na gestão de manutenção predial, monitoramento de consumo de recursos e controle operacional de equipamentos críticos.

A aplicação integra funcionalidades de:

monitoramento de água e energia;
gestão de equipamentos;
manutenção preventiva e corretiva;
registro de rondas operacionais;
emissão de relatórios;
controle de ordens de serviço;
geração de alertas inteligentes baseados em consumo.

O principal objetivo da solução é reduzir desperdícios, aumentar a eficiência operacional e melhorar a tomada de decisão por meio da análise contínua de dados.

🚀 Funcionalidades

✅ Gestão de Equipamentos
Cadastro de equipamentos
Organização por setores
Histórico de manutenção
Controle operacional

✅ Monitoramento de Consumo
Registro diário de consumo
Controle de água
Controle de energia
Comparação com médias esperadas
Identificação de anomalias

✅ Manutenção
Manutenção preventiva
Chamados corretivos
Controle de status
Acompanhamento de prazos
Registro técnico

✅ Rondas Operacionais
Execução de inspeções
Registro de ocorrências
Evidências fotográficas
Histórico completo

✅ Dashboard
Indicadores operacionais
Visão geral do sistema
Alertas automáticos
Dados consolidados

✅ Relatórios
Relatórios técnicos
Histórico operacional
Relatórios de consumo
Controle de atividades



🏗️ Estrutura do Projeto

src/
├── androidTest/
│
├── main/
│   ├── java/com/example/smartsolutionmaintenance/
│   │
│   │   ├── activities/
│   │   │   ├── Activities da aplicação
│   │   │   ├── Telas principais
│   │   │   └── Fluxos do sistema
│   │   │
│   │   ├── adapters/
│   │   │   ├── Adapters de RecyclerView
│   │   │   ├── Listagens dinâmicas
│   │   │   └── Manipulação de itens visuais
│   │   │
│   │   ├── models/
│   │   │   ├── Classes de dados
│   │   │   ├── Entidades do sistema
│   │   │   └── Objetos utilizados na aplicação
│   │   │
│   │   └── utils/
│   │       ├── Funções auxiliares
│   │       ├── Validações
│   │       └── Utilidades gerais
│   │
│   ├── res/
│   │   ├── layout/
│   │   ├── drawable/
│   │   ├── menu/
│   │   ├── values/
│   │   ├── color/
│   │   ├── xml/
│   │   └── mipmap/
│   │
│   └── AndroidManifest.xml
│
├── test/
│
├── build.gradle.kts
├── google-services.json
└── proguard-rules.pro


🧩 Explicação da Arquitetura

O projeto foi organizado seguindo uma estrutura modular simples para facilitar:

manutenção do código;
reutilização de componentes;
organização das responsabilidades;
escalabilidade futura.
📂 activities

Contém todas as telas da aplicação.

Exemplos:

Login
Dashboard
Cadastro de equipamentos
Registro de manutenção
Monitoramento de consumo

Cada Activity é responsável pela interface e interação com o usuário.

📂 adapters

Responsável pelos adapters utilizados em:

RecyclerViews;
listas dinâmicas;
grids;
cards de informações.

Permite renderizar dados dinamicamente nas telas.

📂 models

Contém as classes que representam os dados do sistema.

Exemplos:

Equipamento
Chamado
Consumo
Usuário
Ordem de Serviço

Essas classes são utilizadas para:

armazenamento;
manipulação;
integração com banco de dados e APIs.
📂 utils

Possui funções auxiliares reutilizáveis.

Exemplos:

validações;
máscaras;
formatação;
tratamento de datas;
funções genéricas.
🎨 Recursos de Interface

A pasta res/ contém todos os recursos visuais do aplicativo.

📂 layout

Arquivos XML responsáveis pelas telas da aplicação.

📂 drawable

Ícones, formas, backgrounds e componentes gráficos.

📂 menu

Menus utilizados na navegação da aplicação.

📂 values

Strings, temas, estilos e configurações globais.

📂 color

Paleta de cores do aplicativo.

📂 mipmap

Ícones do aplicativo em diferentes resoluções.

🛠️ Tecnologias Utilizadas
Desenvolvimento Mobile
Java
Android Studio
XML
Controle de Versão
Git
GitHub
Organização do Projeto
ClickUp
Serviços
Firebase (configurado via google-services.json)
📊 Objetivos do Sistema

O sistema busca:

reduzir desperdícios;
melhorar o controle operacional;
aumentar a confiabilidade de equipamentos;
auxiliar a tomada de decisão;
otimizar processos de manutenção.
🌱 Objetivos de Desenvolvimento Sustentável (ODS)

O projeto está alinhado aos seguintes ODS:

ODS 6 — Água potável e saneamento

ODS 7 — Energia acessível e limpa

ODS 9 — Indústria, inovação e infraestrutura

ODS 11 — Cidades e comunidades sustentáveis

ODS 12 — Consumo e produção responsáveis


👥 Público-Alvo

O aplicativo foi desenvolvido para:

instituições filantrópicas;
ONGs;
hospitais beneficentes;
igrejas;
centros comunitários;
equipes de manutenção;
gestores operacionais.

👨‍💻 Equipe do Projeto
Eduardo Junqueira

Renata Andrade

Maria Fernanda Rodrigues de Alencar

Wagner Gutierres Dias

Matheus Filipe Felix

Emily da Mota

▶️ Como Executar o Projeto

Pré-requisitos:
Android Studio instalado,
JDK 17+,
Gradle configurado,
Dispositivo Android ou emulador.
