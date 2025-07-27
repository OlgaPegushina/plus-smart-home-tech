package ru.practicum.kafka.telemetry.model.hub.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.kafka.telemetry.model.hub.enums.ActionType;

/**
 * Представляет действие, которое должно быть выполнено устройством.
 */
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeviceAction {
    String sensorId;
    ActionType type;
    Integer value;
}
