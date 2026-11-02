from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import List

# Инициализация приложения FastAPI
app = FastAPI(
    title="Transport API",
    description="REST API для управления справочником станций",
    version="1.0.0"
)

# --- МОДЕЛИ ДАННЫХ (DTO) ---
class StationCreate(BaseModel):
    name: str
    type: str
    distance: int

class Station(StationCreate):
    id: int

# --- ХРАНИЛИЩЕ В ПАМЯТИ (In-Memory) ---
stations_db: List[Station] = [
    Station(id=1, name="Гомель-Пассажирский", type="Пассажирская", distance=0),
    Station(id=2, name="Василевичи", type="Пассажирская", distance=89)
]
next_id = 3

# --- МАРШРУТЫ (ENDPOINTS) ---

# 1. GET /api/v1/stations/ — получение списка объектов
@app.get("/api/v1/stations/", response_model=List[Station], status_code=200)
def get_stations():
    return stations_db

# 2. GET /api/v1/stations/{id} — получение детальной информации
@app.get("/api/v1/stations/{station_id}", response_model=Station)
def get_station(station_id: int):
    for station in stations_db:
        if station.id == station_id:
            return station
    raise HTTPException(status_code=404, detail="Станция не найдена")

# 3. POST /api/v1/stations/ — создание нового объекта
@app.post("/api/v1/stations/", response_model=Station, status_code=201)
def create_station(station: StationCreate):
    global next_id
    new_station = Station(id=next_id, **station.model_dump())
    stations_db.append(new_station)
    next_id += 1
    return new_station

# 4. DELETE /api/v1/stations/{station_id} — удаление объекта
@app.delete("/api/v1/stations/{station_id}", status_code=200)
def delete_station(station_id: int):
    for i, station in enumerate(stations_db):
        if station.id == station_id:
            stations_db.pop(i)
            return {"message": f"Станция с ID {station_id} удалена"}
    raise HTTPException(status_code=404, detail="Станция не найдена")
