const ChatMessage = ({ message }) => {
  return (
    <div className={`chat-message ${message.fromAdmin ? 'admin' : 'user'}`}>
      <strong>{message.fromAdmin ? 'Support' : message.username}</strong>
      <p>{message.content}</p>
    </div>
  );
};

export default ChatMessage;
