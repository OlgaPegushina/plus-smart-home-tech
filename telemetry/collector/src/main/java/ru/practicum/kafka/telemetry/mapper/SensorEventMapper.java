package ru.practicum.kafka.telemetry.mapper;

import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.kafka.telemetry.model.sensor.SensorEvent;
import ru.practicum.kafka.telemetry.model.sensor.impl.ClimateSensorEvent;
import ru.practicum.kafka.telemetry.model.sensor.impl.LightSensorEvent;
import ru.practicum.kafka.telemetry.model.sensor.impl.MotionSensorEvent;
import ru.practicum.kafka.telemetry.model.sensor.impl.SwitchSensorEvent;
import ru.practicum.kafka.telemetry.model.sensor.impl.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

public class SensorEventMapper {
    public static SensorEventAvro mapToAvro(SensorEvent sensorEvent) {
        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(mapToPayload(sensorEvent))
                .build();
    }

    public static SpecificRecordBase mapToPayload(SensorEvent sensorEvent) {
        if (sensorEvent.getType() == null) {
            throw new IllegalArgumentException("Тип не может быть null");
        }
        return switch (sensorEvent.getType()) {
            case CLIMATE_SENSOR_EVENT -> {
                ClimateSensorEvent climateEvent = (ClimateSensorEvent) sensorEvent;
                yield ClimateSensorAvro.newBuilder()
                        .setTemperatureC(climateEvent.getTemperatureC())
                        .setHumidity(climateEvent.getHumidity())
                        .setCo2Level(climateEvent.getCo2Level())
                        .build();
            }
            case LIGHT_SENSOR_EVENT -> {
                LightSensorEvent lightEvent = (LightSensorEvent) sensorEvent;
                yield LightSensorAvro.newBuilder()
                        .setLinkQuality(lightEvent.getLinkQuality())
                        .setLuminosity(lightEvent.getLuminosity())
                        .build();
            }
            case MOTION_SENSOR_EVENT -> {
                MotionSensorEvent motionEvent = (MotionSensorEvent) sensorEvent;
                yield MotionSensorAvro.newBuilder()
                        .setLinkQuality(motionEvent.getLinkQuality())
                        .setMotion(motionEvent.getMotion())
                        .setVoltage(motionEvent.getVoltage())
                        .build();
            }
            case SWITCH_SENSOR_EVENT -> {
                SwitchSensorEvent switchEvent = (SwitchSensorEvent) sensorEvent;
                yield SwitchSensorAvro.newBuilder()
                        .setState(switchEvent.getState())
                        .build();
            }
            case TEMPERATURE_SENSOR_EVENT -> {
                TemperatureSensorEvent temperatureEvent = (TemperatureSensorEvent) sensorEvent;
                yield TemperatureSensorAvro.newBuilder()
                        .setTemperatureC(temperatureEvent.getTemperatureC())
                        .setTemperatureF(temperatureEvent.getTemperatureF())
                        .build();
            }
        };
    }
}
