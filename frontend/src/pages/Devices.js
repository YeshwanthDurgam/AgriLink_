import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import { FiCpu, FiPlus, FiWifi, FiWifiOff, FiThermometer, FiDroplet, FiSun, FiRefreshCw, FiTrash2, FiSettings } from 'react-icons/fi';
import iotService from '../services/iotService';
import FarmService from '../services/farmService';
import './Devices.css';

const Devices = () => {
  const [devices, setDevices] = useState([]);
  const [farms, setFarms] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedFarm, setSelectedFarm] = useState('');
  const [showAddModal, setShowAddModal] = useState(false);
  const [newDevice, setNewDevice] = useState({
    name: '',
    type: 'SENSOR',
    farmId: '',
    serialNumber: ''
  });

  useEffect(() => {
    fetchFarms();
    fetchDevices();
    fetchAlerts();
  }, []);

  useEffect(() => {
    fetchDevices();
  }, [selectedFarm]);

  const fetchFarms = async () => {
    try {
      const response = await FarmService.getMyFarms();
      let farmsList = [];
      if (Array.isArray(response)) {
        farmsList = response;
      } else if (response && Array.isArray(response.content)) {
        farmsList = response.content;
      }
      setFarms(farmsList);
      if (farmsList.length > 0 && !newDevice.farmId) {
        setNewDevice(prev => ({ ...prev, farmId: farmsList[0].id }));
      }
    } catch (error) {
      console.error('Error fetching farms:', error);
      setFarms([]);
    }
  };

  const fetchDevices = async () => {
    setLoading(true);
    try {
      const farmId = selectedFarm || null;
      const response = await iotService.getDevices(farmId);
      
      if (response.content) {
        setDevices(response.content);
      } else if (Array.isArray(response)) {
        setDevices(response);
      } else {
        setDevices([]);
      }
    } catch (error) {
      console.error('Error fetching devices:', error);
      setDevices([]);
    } finally {
      setLoading(false);
    }
  };

  const fetchAlerts = async () => {
    try {
      const response = await iotService.getAlerts(0, 5, false);
      const alertsList = response.content || response || [];
      setAlerts(alertsList);
    } catch (error) {
      console.error('Error fetching alerts:', error);
      setAlerts([]);
    }
  };

  const handleAddDevice = async (e) => {
    e.preventDefault();
    try {
      await iotService.registerDevice(newDevice);
      toast.success('Device registered successfully');
      setShowAddModal(false);
      setNewDevice({ name: '', type: 'SENSOR', farmId: farms[0]?.id || '', serialNumber: '' });
      fetchDevices();
    } catch (error) {
      toast.error('Failed to register device');
    }
  };

  const handleDeleteDevice = async (deviceId) => {
    if (!window.confirm('Are you sure you want to delete this device?')) return;
    
    try {
      await iotService.deleteDevice(deviceId);
      toast.success('Device deleted successfully');
      fetchDevices();
    } catch (error) {
      toast.error('Failed to delete device');
    }
  };

  const handleAcknowledgeAlert = async (alertId) => {
    try {
      await iotService.acknowledgeAlert(alertId);
      toast.success('Alert acknowledged');
      fetchAlerts();
    } catch (error) {
      toast.error('Failed to acknowledge alert');
    }
  };

  const getDeviceIcon = (type) => {
    switch (type?.toUpperCase()) {
      case 'TEMPERATURE':
        return <FiThermometer />;
      case 'HUMIDITY':
      case 'MOISTURE':
        return <FiDroplet />;
      case 'LIGHT':
        return <FiSun />;
      default:
        return <FiCpu />;
    }
  };

  const getStatusClass = (status) => {
    return status?.toUpperCase() === 'ONLINE' ? 'online' : 'offline';
  };

  return (
    <div className="devices-page">
      <div className="devices-header">
        <h1><FiCpu /> IoT Devices</h1>
        <div className="header-actions">
          <button className="refresh-btn" onClick={fetchDevices}>
            <FiRefreshCw /> Refresh
          </button>
          <button className="add-btn" onClick={() => setShowAddModal(true)}>
            <FiPlus /> Add Device
          </button>
        </div>
      </div>

      {/* Alerts Section */}
      {alerts.length > 0 && (
        <div className="alerts-section">
          <h2>Active Alerts</h2>
          <div className="alerts-list">
            {alerts.map(alert => (
              <div key={alert.id} className={`alert-card ${alert.severity?.toLowerCase()}`}>
                <div className="alert-content">
                  <strong>{alert.title || 'Alert'}</strong>
                  <p>{alert.message}</p>
                  <span className="alert-time">
                    {new Date(alert.createdAt).toLocaleString()}
                  </span>
                </div>
                <button
                  className="acknowledge-btn"
                  onClick={() => handleAcknowledgeAlert(alert.id)}
                >
                  Acknowledge
                </button>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Filter by Farm */}
      <div className="filter-bar">
        <select
          value={selectedFarm}
          onChange={(e) => setSelectedFarm(e.target.value)}
        >
          <option value="">All Farms</option>
          {Array.isArray(farms) && farms.map(farm => (
            <option key={farm.id} value={farm.id}>{farm.name}</option>
          ))}
        </select>
      </div>

      {loading ? (
        <div className="loading-container">
          <div className="spinner"></div>
          <p>Loading devices...</p>
        </div>
      ) : devices.length === 0 ? (
        <div className="empty-state">
          <FiCpu className="empty-icon" />
          <h2>No Devices Found</h2>
          <p>You haven't registered any IoT devices yet.</p>
          <button className="add-btn" onClick={() => setShowAddModal(true)}>
            <FiPlus /> Add Your First Device
          </button>
        </div>
      ) : (
        <div className="devices-grid">
          {devices.map(device => (
            <div key={device.id} className="device-card">
              <div className="device-header">
                <div className="device-icon">
                  {getDeviceIcon(device.type)}
                </div>
                <div className={`device-status ${getStatusClass(device.status)}`}>
                  {device.status === 'ONLINE' ? <FiWifi /> : <FiWifiOff />}
                  {device.status || 'OFFLINE'}
                </div>
              </div>

              <div className="device-info">
                <h3>{device.name}</h3>
                <p className="device-type">{device.type || 'Sensor'}</p>
                {device.farmName && (
                  <p className="device-farm">Farm: {device.farmName}</p>
                )}
                {device.serialNumber && (
                  <p className="device-serial">SN: {device.serialNumber}</p>
                )}
              </div>

              {device.lastReading && (
                <div className="device-reading">
                  <span className="reading-value">{device.lastReading.value}</span>
                  <span className="reading-unit">{device.lastReading.unit}</span>
                  <span className="reading-time">
                    {new Date(device.lastReading.timestamp).toLocaleTimeString()}
                  </span>
                </div>
              )}

              <div className="device-actions">
                <Link to={`/devices/${device.id}`} className="settings-btn">
                  <FiSettings /> Configure
                </Link>
                <button
                  className="delete-btn"
                  onClick={() => handleDeleteDevice(device.id)}
                >
                  <FiTrash2 />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Add Device Modal */}
      {showAddModal && (
        <div className="modal-overlay" onClick={() => setShowAddModal(false)}>
          <div className="modal" onClick={e => e.stopPropagation()}>
            <h2>Register New Device</h2>
            <form onSubmit={handleAddDevice}>
              <div className="form-group">
                <label>Device Name</label>
                <input
                  type="text"
                  value={newDevice.name}
                  onChange={e => setNewDevice(prev => ({ ...prev, name: e.target.value }))}
                  placeholder="e.g., Greenhouse Sensor 1"
                  required
                />
              </div>

              <div className="form-group">
                <label>Device Type</label>
                <select
                  value={newDevice.type}
                  onChange={e => setNewDevice(prev => ({ ...prev, type: e.target.value }))}
                >
                  <option value="SENSOR">General Sensor</option>
                  <option value="TEMPERATURE">Temperature Sensor</option>
                  <option value="HUMIDITY">Humidity Sensor</option>
                  <option value="MOISTURE">Soil Moisture Sensor</option>
                  <option value="LIGHT">Light Sensor</option>
                  <option value="ACTUATOR">Actuator</option>
                </select>
              </div>

              <div className="form-group">
                <label>Farm</label>
                <select
                  value={newDevice.farmId}
                  onChange={e => setNewDevice(prev => ({ ...prev, farmId: e.target.value }))}
                  required
                >
                  <option value="">Select a farm</option>
                  {Array.isArray(farms) && farms.map(farm => (
                    <option key={farm.id} value={farm.id}>{farm.name}</option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label>Serial Number (Optional)</label>
                <input
                  type="text"
                  value={newDevice.serialNumber}
                  onChange={e => setNewDevice(prev => ({ ...prev, serialNumber: e.target.value }))}
                  placeholder="Device serial number"
                />
              </div>

              <div className="modal-actions">
                <button type="button" className="cancel-btn" onClick={() => setShowAddModal(false)}>
                  Cancel
                </button>
                <button type="submit" className="submit-btn">
                  Register Device
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Devices;
