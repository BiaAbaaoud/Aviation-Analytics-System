import requests
import pandas as pd
import os

# --- CONFIGURAÇÃO DE SEGURANÇA ---
# O script tenta buscar a chave no sistema. Se não achar, usa o valor padrão.
# PARA TESTAR LOCAL: Substitua 'SUA_CHAVE_AQUI' pela sua chave real. 
# ANTES DE SUBIR PARA O GIT: Volte para 'SUA_CHAVE_AQUI'.
API_KEY = os.getenv('AVIATION_API_KEY', 'SUA_CHAVE_AQUI')
URL = 'http://api.aviationstack.com/v1/flights'

# --- INTELIGÊNCIA DE FROTA (MAPEAMENTO AGRESSIVO) ---
# Caso a API retorne dados vazios ou códigos técnicos (IATA), o script força o modelo correto.
MEMORIA_FROTA = {
    'BA247': 'A35K', 'IB267': 'A333', 'AZ674': 'A339', 'LA8071': 'B77W',
    'LH506': 'B748', 'SQ2196': 'B748', 'NH5896': 'B748', 'CA6251': 'B748', 
    'AI8898': 'B748', 'KE7967': 'B748', 'JL5513': 'B748', 'CA7109': 'B748', 'AY4425': 'B748'
}

def buscar_dados_gru():
    parametros = {
        'access_key': API_KEY,
        'arr_icao': 'SBGR',
        'flight_status': 'active'
    }
    
    print("📡 Consultando API AviationStack e aplicando filtros...")
    
    try:
        response = requests.get(URL, params=parametros)
        data = response.json()
        lista_final = []

        if 'data' in data:
            for f in data['data']:
                # Verifica se há atraso na chegada
                atraso = f.get('arrival', {}).get('delay')
                
                if atraso and atraso > 0:
                    voo_id = f.get('flight', {}).get('iata', 'N/A')
                    ac = f.get('aircraft', {})
                    
                    # Lógica de Identificação de Modelo:
                    # 1. Tenta pegar o código IATA da API
                    # 2. Se falhar, tenta pegar o código ICAO
                    # 3. Se ainda for vazio/N/D, busca na nossa MEMORIA_FROTA pelo número do voo
                    modelo = ac.get('iata') or ac.get('icao') or "N/D"
                    
                    if modelo == "N/D" or modelo is None:
                        modelo = MEMORIA_FROTA.get(voo_id, "N/D")
                    
                    lista_final.append({
                        'voo': voo_id,
                        'modelo': modelo,
                        'origem': f.get('departure', {}).get('iata', 'N/A'),
                        'destino': 'GRU',
                        'status': 'ATRASADO',
                        'atraso': f"{atraso} min"
                    })

        # --- PERSISTÊNCIA DOS DADOS (CSV MIDDLEWARE) ---
        if lista_final:
            try:
                df = pd.DataFrame(lista_final)
                df.to_csv('dados_atrasos.csv', index=False, encoding='utf-8')
                print(f"✅ Sincronização concluída: {len(lista_final)} voos críticos em sistema.")
            except PermissionError:
                print("⚠️ ERRO DE PERMISSÃO: O arquivo 'dados_atrasos.csv' pode estar aberto no Excel. Feche-o para atualizar.")
        else:
            print("ℹ️ Nenhum voo com atraso detectado no momento.")