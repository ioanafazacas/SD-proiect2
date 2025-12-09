import React, { useEffect, useState, useContext } from "react";
import { deviceAPI } from "../api/devices.js";
import { AuthContext } from "../context/AuthContext";
import Navbar from "../components/Navbar";
import DeviceChartModal from "../components/DeviceCharModal.jsx";

export default function UserDashboard() {
  const { user, token } = useContext(AuthContext);
  const [devices, setDevices] = useState([]);

  const [selectedDevice, setSelectedDevice] = useState(null);
  const [chartOpen, setChartOpen] = useState(false);

  useEffect(() => {
    if (!user || !user.user_id) return;

    deviceAPI
      .get(`/device/user/${user.user_id}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setDevices(res.data))
      .catch((err) => console.error("Error loading devices:", err));
  }, [user, token]);

  const openChart = (device) => {
    setSelectedDevice(device);
    setChartOpen(true);
  };

  const closeChart = () => {
    setChartOpen(false);
    setSelectedDevice(null);
  };

  return (
    <div>
      <Navbar />

      <h1 className="text-3xl text-center mt-6 font-semibold">
        My Devices
      </h1>

      <div className="p-6 max-w-4xl mx-auto">
        {devices.length === 0 && (
          <p className="text-center text-gray-500">
            You don't have any devices registered.
          </p>
        )}

        {devices.map((d) => (
          <div
            key={d.deviceId}
            className="border p-4 mb-3 rounded-lg shadow-sm flex items-center justify-between bg-white"
          >
            <div>
              <p className="text-lg font-bold">{d.name}</p>
              <p className="text-sm text-gray-600">Serial: {d.serialNumber}</p>
              <p className="text-sm text-gray-600">
                Max consumption: {d.maxConsumption}W
              </p>
            </div>

            <button
              className="bg-blue-500 hover:bg-blue-600 text-white px-5 py-2 rounded shadow"
              onClick={() => openChart(d)}
            >
              View Chart
            </button>
          </div>
        ))}
      </div>

      {/* Modal component */}
      <DeviceChartModal
        open={chartOpen}
        onClose={closeChart}
        device={selectedDevice}
      />
    </div>
  );
}
