import { useState } from "react";
import { useNavigate } from "react-router-dom";
import LoginModal from "../components/LoginModal";

export default function Register() {
  const [user, setUser] = useState("");
  const [pass, setPass] = useState("");
  const [showLoginModal, setShowLoginModal] = useState(false);

  const navigate = useNavigate();

  const isLoggedIn = () => {
    // Adjust this key to match however your app stores the session
    return !!localStorage.getItem("loggedInUser");
  };

  const handleSubmit = () => {
    if (!isLoggedIn()) {
      // User is not logged in → show login popup
      setShowLoginModal(true);
      return;
    }

    // User is already logged in → proceed with registration
    if (!user || !pass) {
      alert("Please fill in all fields.");
      return;
    }

    localStorage.setItem("user_" + user, pass);
    alert("Registered successfully!");
    navigate("/login");
  };

  return (
    <div style={{ padding: 40 }}>
      <h2>Register</h2>

      <input
        placeholder="Username"
        value={user}
        onChange={(e) => setUser(e.target.value)}
      />

      <br /><br />

      <input
        type="password"
        placeholder="Password"
        value={pass}
        onChange={(e) => setPass(e.target.value)}
      />

      <br /><br />

      <button onClick={handleSubmit}>Register</button>

      {/* Login popup — shown when user is not logged in */}
      {showLoginModal && (
        <LoginModal
          onClose={() => setShowLoginModal(false)}
          onLoginSuccess={() => {
            setShowLoginModal(false);
            // Optionally retry registration after login:
            // handleSubmit();
          }}
        />
      )}
    </div>
  );
}