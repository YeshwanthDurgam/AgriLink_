import { iotApi } from './api';

const iotService = {
  // Get all devices for the user's farms
  getDevices: async (farmId = null, page = 0, size = 10) => {
    const params = { page, size };
    if (farmId) params.farmId = farmId;
    const response = await iotApi.get('/devices', { params });
    return response.data;
  },

  // Get device by ID
  getDeviceById: async (id) => {
    const response = await iotApi.get(`/devices/${id}`);
    return response.data;
  },

  // Register a new device
  registerDevice: async (deviceData) => {
    const response = await iotApi.post('/devices', deviceData);
    return response.data;
  },

  // Update device
  updateDevice: async (id, deviceData) => {
    const response = await iotApi.put(`/devices/${id}`, deviceData);
    return response.data;
  },

  // Delete device
  deleteDevice: async (id) => {
    const response = await iotApi.delete(`/devices/${id}`);
    return response.data;
  },

  // Get telemetry data for a device
  getDeviceTelemetry: async (deviceId, startTime, endTime, limit = 100) => {
    const response = await iotApi.get(`/telemetry/device/${deviceId}`, {
      params: { startTime, endTime, limit }
    });
    return response.data;
  },

  // Get latest telemetry for a device
  getLatestTelemetry: async (deviceId) => {
    const response = await iotApi.get(`/telemetry/device/${deviceId}/latest`);
    return response.data;
  },

  // Get alerts for devices
  getAlerts: async (page = 0, size = 10, acknowledged = null) => {
    const params = { page, size };
    if (acknowledged !== null) params.acknowledged = acknowledged;
    const response = await iotApi.get('/alerts', { params });
    return response.data;
  },

  // Acknowledge an alert
  acknowledgeAlert: async (alertId) => {
    const response = await iotApi.post(`/alerts/${alertId}/acknowledge`);
    return response.data;
  },
};

export default iotService;
