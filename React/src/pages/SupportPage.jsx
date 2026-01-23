import ChatWidget from '../components/chat/ChatWidget';
import { useAuth } from '../context/AuthContext';

const SupportPage = () => {
  const { user } = useAuth();

  return (
    <div>
      <h2>Customer Support</h2>
      <ChatWidget user={user} />
    </div>
  );
};

export default SupportPage;
