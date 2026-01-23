import { useEffect, useState } from 'react';
import { connectChat, sendChatMessage } from '../../services/chatWebSocket';
import ChatMessage from './ChatMessage';
import ChatInput from './ChatInput';
import './chat.css';

const ChatWidget = ({ user }) => {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    connectChat(user.id, (msg) => {
      setMessages((prev) => [...prev, msg]);
    });
  }, [user.id]);

  const handleSend = (text) => {
    const message = {
      userId: user.id,
      username: user.username,
      content: text,
      fromAdmin: false,
    };

    setMessages((prev) => [...prev, message]);
    sendChatMessage(message);
  };

  return (
    <div className="chat-container">
      <div className="chat-messages">
        {messages.map((m, i) => (
          <ChatMessage key={i} message={m} />
        ))}
      </div>
      <ChatInput onSend={handleSend} />
    </div>
  );
};

export default ChatWidget;
