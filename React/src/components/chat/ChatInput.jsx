import { useState } from 'react';

const ChatInput = ({ onSend }) => {
  const [text, setText] = useState('');

  const send = () => {
    if (text.trim()) {
      onSend(text);
      setText('');
    }
  };

  return (
    <div className="chat-input">
      <input
        value={text}
        onChange={(e) => setText(e.target.value)}
        onKeyDown={(e) => e.key === 'Enter' && send()}
        placeholder="Type your message..."
      />
      <button onClick={send}>Send</button>
    </div>
  );
};

export default ChatInput;
