import React from 'react';
import EnergyConsumptionCharts from './EnergyConsumptionCharts.jsx';

export default function DeviceChartModal({ open, onClose, device }) {
  if (!open || !device) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center p-4">
      <div className="bg-white p-6 rounded-xl w-full max-w-4xl shadow-lg relative">
        {/* Close button */}
        <button
          onClick={onClose}
          className="absolute top-3 right-3 text-xl font-bold text-gray-700 hover:text-black"
        >
          ✖
        </button>

        <h2 className="text-xl font-semibold mb-4">
          Energy Consumption – {device.name}
        </h2>

        {/* Chart component */}
        <EnergyConsumptionCharts
          deviceId={device.deviceId}
          deviceName={device.name}
        />
      </div>
    </div>
  );
}
