package com.iclassq.video.exception;

public class DeviceNotAssignedException extends RuntimeException {
    public DeviceNotAssignedException(String message) {
        super(message);
    }

    public DeviceNotAssignedException(Integer deviceId) {
        super(String.format("Dispositivo con ID %d no tiene área asignada", deviceId));
    }
}
