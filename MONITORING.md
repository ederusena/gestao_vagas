# Configuração Prometheus + Grafana

## Pré-requisitos
- Docker e Docker Compose instalados
- Aplicação Spring Boot rodando na porta 8080

## Como usar

### 1. Iniciar a aplicação Spring Boot
```bash
mvn spring-boot:run
```

### 2. Iniciar Prometheus e Grafana
```bash
docker-compose -f docker-compose-monitoring.yml up -d
```

### 3. Acessar as ferramentas

#### Prometheus
- URL: http://localhost:9090
- Verificar targets: http://localhost:9090/targets
- O target `spring-boot-application` deve estar com status "UP"

#### Grafana
- URL: http://localhost:3000
- Usuário: `admin`
- Senha: `admin`
- O datasource Prometheus já está configurado automaticamente

### 4. Métricas disponíveis

A aplicação expõe métricas através do endpoint:
- http://localhost:8080/actuator/prometheus

Algumas métricas úteis:
- `jvm_memory_used_bytes` - Uso de memória JVM
- `jvm_threads_live_threads` - Threads ativas
- `http_server_requests_seconds_count` - Contagem de requisições HTTP
- `http_server_requests_seconds_sum` - Tempo total de requisições
- `system_cpu_usage` - Uso de CPU do sistema
- `process_cpu_usage` - Uso de CPU do processo

### 5. Criar dashboards no Grafana

#### Dashboard básico de Spring Boot:

1. No Grafana, clique em "+" → "Dashboard" → "Add visualization"
2. Selecione o datasource "Prometheus"
3. Adicione queries como:
   - Taxa de requisições: `rate(http_server_requests_seconds_count[5m])`
   - Latência média: `rate(http_server_requests_seconds_sum[5m]) / rate(http_server_requests_seconds_count[5m])`
   - Uso de memória: `jvm_memory_used_bytes{area="heap"}`
   - CPU: `system_cpu_usage`

#### Importar dashboard pronto:

1. Vá em "+" → "Import dashboard"
2. Use o ID `4701` (Spring Boot 2.1 System Monitor)
3. Ou o ID `11378` (JVM Micrometer)
4. Selecione o datasource Prometheus

### 6. Parar os serviços

```bash
docker-compose -f docker-compose-monitoring.yml down
```

Para remover os volumes (dados persistentes):
```bash
docker-compose -f docker-compose-monitoring.yml down -v
```

## Estrutura de arquivos

```
gestao_vagas/
├── prometheus.yml                              # Configuração do Prometheus
├── docker-compose-monitoring.yml               # Docker Compose para monitoring
└── grafana/
    └── provisioning/
        ├── datasources/
        │   └── prometheus.yml                  # Datasource auto-configurado
        └── dashboards/
            └── dashboard.yml                   # Provisionamento de dashboards
```

## Troubleshooting

### Prometheus não consegue acessar a aplicação
- Verifique se a aplicação está rodando em `localhost:8080`
- Verifique se o endpoint `/actuator/prometheus` está acessível
- No Windows, o `host.docker.internal` deve funcionar automaticamente

### Grafana não mostra dados
- Verifique se o Prometheus está coletando métricas em http://localhost:9090/targets
- Verifique se o datasource está configurado corretamente em Configuration → Data sources

### Porta já em uso
- Se as portas 9090 ou 3000 já estiverem em uso, edite o `docker-compose-monitoring.yml` para usar portas diferentes
