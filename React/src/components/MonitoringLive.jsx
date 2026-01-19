import { useEffect } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const MonitoringLive = () => {
  useEffect(() => {
    const socket = new SockJS('http://localhost:8085/ws');

    const client = new Client({
      webSocketFactory: () => socket,
      debug: function (str) {
        console.log(str);
      },
      onConnect: () => {
        console.log('Connected to WebSocket');

        client.subscribe('/topic/monitoring', (message) => {
          const data = JSON.parse(message.body);
          console.log('LIVE DATA:', data);
        });
      },
    });

    client.activate();

    return () => {
      client.deactivate();
    };
  }, []);

  return (
    <div>
      <h2>Live Monitoring</h2>
      <p>Check console for real-time data</p>
    </div>
  );
};

export default MonitoringLive;
