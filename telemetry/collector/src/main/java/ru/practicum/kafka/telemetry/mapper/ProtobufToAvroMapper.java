package ru.practicum.kafka.telemetry.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.ConditionOperationProto;
import ru.yandex.practicum.grpc.telemetry.event.ConditionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.LightSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.MotionSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SwitchSensorProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import com.google.protobuf.Timestamp;
import java.time.Instant;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ProtobufToAvroMapper {
    @Mapping(source = "hubId",    target = "hubId")
    @Mapping(source = "timestamp", target = "timestamp")
    @Mapping(target = "payload",
            expression = "java(mapHubEventPayload(event))")
    HubEventAvro toAvro(HubEventProto event);

    @Mapping(source = "id",        target = "id")
    @Mapping(source = "hubId",     target = "hubId")
    @Mapping(source = "timestamp", target = "timestamp")
    @Mapping(target = "payload",
            expression = "java(mapSensorEventPayload(event))")
    SensorEventAvro toAvro(SensorEventProto event);

    DeviceAddedEventAvro   toPayload(DeviceAddedEventProto e);
    DeviceRemovedEventAvro toPayload(DeviceRemovedEventProto e);

    @Mapping(source = "name",          target = "name")
    @Mapping(source = "conditionList", target = "conditions")
    @Mapping(source = "actionList",    target = "actions")
    ScenarioAddedEventAvro toPayload(ScenarioAddedEventProto e);

    ScenarioRemovedEventAvro toPayload(ScenarioRemovedEventProto e);

    ClimateSensorAvro     toPayload(ClimateSensorProto e);
    LightSensorAvro       toPayload(LightSensorProto e);
    MotionSensorAvro      toPayload(MotionSensorProto e);
    SwitchSensorAvro      toPayload(SwitchSensorProto e);
    TemperatureSensorAvro toPayload(TemperatureSensorProto e);

    @Mapping(source = "sensorId",  target = "sensorId")
    @Mapping(source = "type",      target = "type")
    @Mapping(source = "operation", target = "operation")
    @Mapping(source = ".",         target = "value", qualifiedByName = "mapConditionValue")
    ScenarioConditionAvro toPayload(ScenarioConditionProto c);

    @Mapping(source = "sensorId", target = "sensorId")
    @Mapping(source = "type",     target = "type")
    @Mapping(source = ".",        target = "value", qualifiedByName = "mapActionValue")
    DeviceActionAvro toPayload(DeviceActionProto a);

    //  Вспомогательные "ручные" методы

    /** Разворачиваем oneof-пейлоады HubEventProto */
    default Object mapHubEventPayload(HubEventProto e) {
        if (e.hasDeviceAdded())     return toPayload(e.getDeviceAdded());
        if (e.hasDeviceRemoved())   return toPayload(e.getDeviceRemoved());
        if (e.hasScenarioAdded())   return toPayload(e.getScenarioAdded());
        if (e.hasScenarioRemoved()) return toPayload(e.getScenarioRemoved());
        return null;
    }

    /** Разворачиваем oneof-пейлоады SensorEventProto */
    default Object mapSensorEventPayload(SensorEventProto e) {
        if (e.hasMotionSensorEvent())      return toPayload(e.getMotionSensorEvent());
        if (e.hasTemperatureSensorEvent()) return toPayload(e.getTemperatureSensorEvent());
        if (e.hasLightSensorEvent())       return toPayload(e.getLightSensorEvent());
        if (e.hasClimateSensorEvent())     return toPayload(e.getClimateSensorEvent());
        if (e.hasSwitchSensorEvent())      return toPayload(e.getSwitchSensorEvent());
        return null;
    }

    /** Protobuf Timestamp в java.time.Instant */
    default Instant map(Timestamp ts) {
        if (ts == null) return null;
        return Instant.ofEpochSecond(ts.getSeconds(), ts.getNanos());
    }

    /** union{null,int,boolean} для ScenarioConditionAvro.value */
    @Named("mapConditionValue")
    default Object mapConditionValue(ScenarioConditionProto c) {
        if (c.hasIntValue())  return c.getIntValue();
        if (c.hasBoolValue()) return c.getBoolValue();
        return null;
    }

    /** optional int32 в Integer для DeviceActionAvro.value */
    @Named("mapActionValue")
    default Integer mapActionValue(DeviceActionProto a) {
        return a.hasValue() ? a.getValue() : null;
    }

    //enum
    default DeviceTypeAvro map(DeviceTypeProto p) {
        if (p == null) return null;
        switch (p) {
            case MOTION_SENSOR: return DeviceTypeAvro.MOTION_SENSOR;
            case TEMPERATURE_SENSOR: return DeviceTypeAvro.TEMPERATURE_SENSOR;
            case LIGHT_SENSOR: return DeviceTypeAvro.LIGHT_SENSOR;
            case CLIMATE_SENSOR: return DeviceTypeAvro.CLIMATE_SENSOR;
            case SWITCH_SENSOR: return DeviceTypeAvro.SWITCH_SENSOR;
            case UNRECOGNIZED:
            default: return null;
        }
    }

    default ActionTypeAvro map(ActionTypeProto p) {
        if (p == null) return null;
        switch (p) {
            case ACTIVATE: return ActionTypeAvro.ACTIVATE;
            case DEACTIVATE: return ActionTypeAvro.DEACTIVATE;
            case INVERSE: return ActionTypeAvro.INVERSE;
            case SET_VALUE: return ActionTypeAvro.SET_VALUE;
            case UNRECOGNIZED:
            default: return null;
        }
    }

    default ConditionTypeAvro map(ConditionTypeProto p) {
        if (p == null) return null;
        switch (p) {
            case MOTION: return ConditionTypeAvro.MOTION;
            case LUMINOSITY: return ConditionTypeAvro.LUMINOSITY;
            case SWITCH: return ConditionTypeAvro.SWITCH;
            case TEMPERATURE: return ConditionTypeAvro.TEMPERATURE;
            case CO2LEVEL: return ConditionTypeAvro.CO2LEVEL;
            case HUMIDITY: return ConditionTypeAvro.HUMIDITY;
            case UNRECOGNIZED:
            default: return null;
        }
    }

    default ConditionOperationAvro map(ConditionOperationProto p) {
        if (p == null) return null;
        switch (p) {
            case EQUALS: return ConditionOperationAvro.EQUALS;
            case GREATER_THAN: return ConditionOperationAvro.GREATER_THAN;
            case LOWER_THAN: return ConditionOperationAvro.LOWER_THAN;
            case UNRECOGNIZED:
            default: return null;
        }
    }
}