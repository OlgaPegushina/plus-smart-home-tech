package ru.practicum.kafka.telemetry.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.kafka.telemetry.model.hub.impl.DeviceAddedEvent;
import ru.practicum.kafka.telemetry.model.hub.impl.DeviceRemovedEvent;
import ru.practicum.kafka.telemetry.model.hub.impl.ScenarioAddedEvent;
import ru.practicum.kafka.telemetry.model.hub.impl.ScenarioRemovedEvent;
import ru.practicum.kafka.telemetry.model.sensor.impl.ClimateSensorEvent;
import ru.practicum.kafka.telemetry.model.sensor.impl.LightSensorEvent;
import ru.practicum.kafka.telemetry.model.sensor.impl.MotionSensorEvent;
import ru.practicum.kafka.telemetry.model.sensor.impl.SwitchSensorEvent;
import ru.practicum.kafka.telemetry.model.sensor.impl.TemperatureSensorEvent;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "payload", source = "climateSensorEvent")
    SensorEventAvro toAvro(ClimateSensorEvent climateSensorEvent);

    @Mapping(target = "payload", source = "lightSensorEvent")
    SensorEventAvro toAvro(LightSensorEvent lightSensorEvent);

    @Mapping(target = "payload", source = "motionSensorEvent")
    SensorEventAvro toAvro(MotionSensorEvent motionSensorEvent);

    @Mapping(target = "payload", source = "switchSensorEvent")
    SensorEventAvro toAvro(SwitchSensorEvent switchSensorEvent);

    @Mapping(target = "payload", source = "temperatureSensorEvent")
    SensorEventAvro toAvro(TemperatureSensorEvent temperatureSensorEvent);

    @Mapping(target = "payload", source = "deviceAddedEvent")
    HubEventAvro toAvro(DeviceAddedEvent deviceAddedEvent);

    @Mapping(target = "payload", source = "deviceRemovedEvent")
    HubEventAvro toAvro(DeviceRemovedEvent deviceRemovedEvent);

    @Mapping(target = "payload", source = "scenarioAddedEvent")
    HubEventAvro toAvro(ScenarioAddedEvent scenarioAddedEvent);

    @Mapping(target = "payload", source = "scenarioRemovedEvent")
    HubEventAvro toAvro(ScenarioRemovedEvent scenarioRemovedEvent);
}
