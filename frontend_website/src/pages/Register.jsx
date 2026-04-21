import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { register } from "../api/authService";  // ← real backend call

export default function Register() {

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phone, setPhone] = useState("");
  const [pincode, setPincode] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleRegister = async () => {

    if (!name || !email || !password || !phone || !pincode) {
      setError("Please fill all fields");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const response = await register({ name, email, password, phone, pincode });
      const { token, role } = response.data;

      // Save token after successful registration
      localStorage.setItem("token", token);
      localStorage.setItem("role", role);

      alert("Registered successfully!");
      navigate("/report");

    } catch (err) {
      if (err.response?.data?.message) {
        setError(err.response.data.message);  // show backend error message
      } else {
        setError("Registration failed. Please try again.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: 40 }}>
      <h2>Register</h2>

      <input
        placeholder="Full Name"
        value={name}
        onChange={(e) => setName(e.target.value)}
      />

      <br /><br />

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

      <input
        placeholder="Phone Number"
        value={phone}
        onChange={(e) => setPhone(e.target.value)}
      />

      <br /><br />

      <input
        placeholder="Pincode"
        value={pincode}
        onChange={(e) => setPincode(e.target.value)}
      />

      <br /><br />

      {error && <p style={{ color: "red" }}>{error}</p>}

      <button onClick={handleRegister} disabled={loading}>
        {loading ? "Registering..." : "Register"}
      </button>

      <br /><br />

      <p>
        Already have an account? <a href="/login">Login</a>
      </p>

    </div>
  );
}