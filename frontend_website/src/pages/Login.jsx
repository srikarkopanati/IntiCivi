import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../api/authService";  // ← changed from utils/auth

export default function Login() {
  const [email, setEmail] = useState("");        // ← changed from username to email
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleLogin = async () => {
    if (!email || !password) {
      setError("Please enter email and password");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const response = await login({ email, password });
      const { token, role } = response.data;

      // Save token to localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("role", role);

      // Redirect based on role
      if (role === "ADMIN") {
        navigate("/report");
      } else {
        navigate("/report");
      }

    } catch (err) {
      setError("Invalid email or password");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: 40 }}>
      <h2>Login</h2>

      <input
        placeholder="Email"              
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />

      <br /><br />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />

      <br /><br />

      {error && <p style={{ color: "red" }}>{error}</p>}

      <button onClick={handleLogin} disabled={loading}>
        {loading ? "Logging in..." : "Login"}
      </button>

      <br /><br />

      <p>
        New user? <a href="/register">Register</a>
      </p>
    </div>
  );
}