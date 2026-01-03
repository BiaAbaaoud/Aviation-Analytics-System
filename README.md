# ✈️ Real-Time Airport Operations Monitor - SBGR (Guarulhos)

![Status](https://img.shields.io/badge/Status-Conclu%C3%ADdo-brightgreen)
![Python](https://img.shields.io/badge/Python-3.x-blue?logo=python&logoColor=white)
![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)
![Windows](https://img.shields.io/badge/OS-Windows-0078D6?logo=windows&logoColor=white)

## 📌 Sobre o Projeto
Este sistema é uma solução completa de monitoramento para o Aeroporto Internacional de Guarulhos (**SBGR**). Ele combina a agilidade do **Python** para processamento de dados com a robustez do **Java Swing** para a interface do usuário, criando um painel de controle que se atualiza automaticamente a cada 60 segundos.

O objetivo principal é identificar voos atrasados, traduzir códigos técnicos da aviação para nomes amigáveis e estimar o impacto de passageiros (**PAX**) no terminal, auxiliando na tomada de decisão operacional.

---

## 🎯 Objetivos do Projeto
- **Monitoramento em Tempo Real:** Capturar dados vivos da API AviationStack.
- **Inteligência de Negócio:** Estimar o número de passageiros afetados com base no modelo da aeronave.
- **Interoperabilidade:** Demonstrar a integração entre scripts Python e aplicações Java.
- **Automação:** Criar um ciclo de atualização autônomo sem intervenção humana.

---

## 🛠️ Ferramentas e Tecnologias

### **Back-end & Data Processing (Python)**
- **Requests:** Consumo da API REST AviationStack.
- **Pandas:** Estruturação e limpeza de dados brutos.
- **CSV Middleware:** Utilizado como camada de persistência de dados entre as linguagens.
- **Dicionário de Frota:** Lógica personalizada para mapear códigos IATA (ex: `B38M`, `A35K`) para nomes comerciais.

### **Front-end & Lógica de Interface (Java)**
- **Java Swing:** Interface desktop profissional e intuitiva.
- **javax.swing.Timer:** Gerenciamento da Thread de atualização automática.
- **Runtime Exec:** Orquestração de processos externos (Java chamando o interpretador Python).

### **Automação (Windows Batch)**
- **Batch Script (.bat):** Orquestrador do pipeline de compilação e execução.

---

## 🏗️ Arquitetura do Sistema
1. **Trigger:** O Java dispara um evento interno a cada 60 segundos.
2. **Coleta:** O script Python é invocado, consulta a API e gera o arquivo `dados_atrasos.csv`.
3. **Processamento:** O Java lê o arquivo, aplica a lógica de tradução e calcula o impacto de PAX.
4. **Visualização:** A interface GUI limpa a tabela e renderiza os novos dados com timestamp.

---

## 📊 Regras de Negócio (Estimativa de PAX)
O sistema utiliza uma lógica de cálculo baseada na capacidade média configurada para cada aeronave:

| Modelo de Aeronave | Capacidade Estimada (PAX) |
| :--- | :--- |
| **Boeing 747-8** | 467 passageiros |
| **Boeing 777** | 410 passageiros |
| **Airbus A350** | 350 passageiros |
| **Airbus A330** | 298 passageiros |
| **Narrow Body (737/A320)** | 174 - 180 passageiros |

---

## 🧠 Desafios Superados
- **Qualidade dos Dados:** Tratamento de retornos "N/D" da API através de uma "Memória de Frota" por número de voo.
- **Concorrência de Arquivos:** Tratamento de exceções para evitar crashes caso o CSV esteja aberto no Excel.
- **Sincronização:** Garantia de que o Java aguarde o processo do Python finalizar antes de atualizar a GUI.

---

## 🕹️ Como Executar
1. Clone o repositório.
2. Obtenha uma chave de API em [AviationStack](https://aviationstack.com/).
3. **Segurança:** Insira sua chave no arquivo `coleta_dados.py` na variável `API_KEY`.
   > ⚠️ **Atenção:** Nunca suba sua chave de API para repositórios públicos! O projeto inclui um `.gitignore` para proteção.
4. Certifique-se de ter Python e JDK 17+ instalados.
5. Execute o arquivo `START_MONITOR.bat`.

---

## 👤 Desenvolvedora
**Desenvolvido por [BiaAbaaoud](https://github.com/BiaAbaaoud)**