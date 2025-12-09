import React, { useState, useEffect, useContext } from 'react';
import {
  LineChart,
  Line,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import { monitoringApi } from '../api/monitoring';
import '../styles/EnergyConsumptionChart.css';
import { AuthContext } from '../context/AuthContext';

const EnergyConsumptionCharts = ({ deviceId, deviceName }) => {
  const { user, token } = useContext(AuthContext);   // <-- We take token here

  const [selectedDate, setSelectedDate] = useState(new Date());
  const [chartType, setChartType] = useState('line');
  const [consumptionData, setConsumptionData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (deviceId) {
      fetchConsumptionData();
    }
  }, [deviceId, selectedDate]);

  const fetchConsumptionData = async () => {
    if (!deviceId || !token) return;

    setLoading(true);
    setError(null);

    try {
      const formattedDate = selectedDate.toISOString().substring(0, 10);

      const res = await monitoringApi.get(
        `/consumption/daily/${deviceId}?date=${formattedDate}`,
        {
          headers: {
            Authorization: `Bearer ${token}`, // <-- Added like your example
          },
        }
      );

      const data = res.data;

      const hourly = Array.from({ length: 24 }, (_, hour) => {
        const entry = data.find(
          (d) => new Date(d.hourTimestamp).getHours() === hour
        );

        return {
          hour: `${String(hour).padStart(2, '0')}:00`,
          energy: entry ? entry.totalConsumption : 0,
          exceeded: entry ? entry.exceeded : false,
          maxThreshold: entry ? entry.maxConsumptionThreshold : 0,
        };
      });

      setConsumptionData(hourly);
    } catch (err) {
      console.error('Fetch error:', err);
      setError('Failed to load consumption data.');
    } finally {
      setLoading(false);
    }
  };

  const handleDateChange = (e) => {
    setSelectedDate(new Date(e.target.value));
  };

  const formatDateForInput = (date) =>
    date.toISOString().substring(0, 10);

  const CustomTooltip = ({ active, payload }) => {
    if (!active || !payload?.length) return null;

    const d = payload[0].payload;
    return (
      <div className="custom-tooltip">
        <p>
          <strong>{d.hour}</strong>
        </p>
        <p style={{ color: d.exceeded ? 'red' : '#8884d8' }}>
          {d.energy.toFixed(2)} kWh
        </p>
        {d.exceeded && <p>⚠ Exceeded!</p>}
      </div>
    );
  };

  return (
    <div className="energy-chart-container">
      <h2>Energy Consumption – {deviceName}</h2>

      <div className="chart-controls">
        <input
          type="date"
          value={formatDateForInput(selectedDate)}
          onChange={handleDateChange}
        />

        <div>
          <button onClick={() => setChartType('line')}>📈 Line</button>
          <button onClick={() => setChartType('bar')}>📊 Bar</button>
        </div>
      </div>

      {loading && <div>Loading...</div>}
      {error && <div style={{ color: 'red' }}>{error}</div>}

      {!loading && !error && (
        <ResponsiveContainer width="100%" height={400}>
          {chartType === 'line' ? (
            <LineChart data={consumptionData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="hour" />
              <YAxis />
              <Tooltip content={<CustomTooltip />} />
              <Legend />
              <Line
                type="monotone"
                dataKey="energy"
                stroke="#8884d8"
                dot={{ r: 4 }}
              />
            </LineChart>
          ) : (
            <BarChart data={consumptionData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="hour" />
              <YAxis />
              <Tooltip content={<CustomTooltip />} />
              <Legend />
              <Bar
                dataKey="energy"
                fill="#8884d8"
                label={{ position: 'top' }}
              />
            </BarChart>
          )}
        </ResponsiveContainer>
      )}
    </div>
  );
};

export default EnergyConsumptionCharts;
