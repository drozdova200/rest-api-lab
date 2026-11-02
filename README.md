# rest-api-lab

Лабораторная работа 6: REST API, OpenAPI, Docker, GitHub Actions

## Запуск проекта

1. Установить зависимости:
   ```bash
   pip install -r requirements.txt

   2. Запустить сервер: uvicorn main:app --reload

3. Открыть документацию: http://127.0.0.1:8000/docs

Структура проекта:
- main.py — исходный код REST API
- Dockerfile — файл для Docker
- docker-compose.yml — запуск через Docker Compose
- requirements.txt — зависимости
- .github/workflows/ci.yml — CI-конвейер
