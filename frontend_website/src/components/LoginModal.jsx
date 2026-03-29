import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login, isLoggedIn } from "../utils/auth";

const modalStyles = `
  @import url('https://fonts.googleapis.com/css2?family=EB+Garamond:wght@400;500;600&family=DM+Sans:wght@300;400;500;600&display=swap');

  .lm-overlay {
    position: fixed; inset: 0;
    background: rgba(10, 34, 64, 0.6);
    backdrop-filter: blur(4px);
    display: flex; align-items: center; justify-content: center;
    z-index: 999;
    animation: lm-fadeIn 0.2s ease;
  }
  @keyframes lm-fadeIn { from { opacity: 0; } to { opacity: 1; } }

  .lm-card {
    background: #fff;
    border-radius: 8px;
    width: 100%; max-width: 400px;
    box-shadow: 0 24px 60px rgba(10,34,64,0.18), 0 4px 16px rgba(10,34,64,0.1);
    overflow: hidden;
    animation: lm-slideUp 0.25s ease;
  }
  @keyframes lm-slideUp {
    from { transform: translateY(18px); opacity: 0; }
    to   { transform: translateY(0);    opacity: 1; }
  }

  .lm-head {
    background: linear-gradient(135deg, #0a2240 0%, #14355e 100%);
    padding: 24px 28px 20px;
    display: flex; align-items: flex-start; justify-content: space-between;
  }
  .lm-head-logo { font-size: 26px; margin-bottom: 6px; }
  .lm-head h2 {
    font-family: 'EB Garamond', serif;
    font-size: 22px; font-weight: 500;
    color: #fff; margin: 0 0 4px;
    letter-spacing: -0.01em;
  }
  .lm-head p { font-size: 12.5px; color: rgba(255,255,255,0.55); margin: 0; }
  .lm-close {
    background: rgba(255,255,255,0.1); border: none;
    color: rgba(255,255,255,0.7); cursor: pointer;
    width: 30px; height: 30px; border-radius: 50%;
    font-size: 16px; display: flex; align-items: center; justify-content: center;
    transition: background 0.15s; flex-shrink: 0; margin-left: 12px;
  }
  .lm-close:hover { background: rgba(255,255,255,0.2); color: #fff; }

  .lm-body { padding: 26px 28px 28px; }

  .lm-tab-row {
    display: flex; gap: 0; margin-bottom: 22px;
    border: 1.5px solid #dedad2; border-radius: 5px; overflow: hidden;
  }
  .lm-tab {
    flex: 1; padding: 9px; border: none; background: #faf8f3;
    font-family: 'DM Sans', sans-serif; font-size: 13px; font-weight: 500;
    color: #8a8780; cursor: pointer; transition: all 0.15s;
  }
  .lm-tab.active { background: #0a2240; color: #fff; }
  .lm-tab:first-child { border-right: 1.5px solid #dedad2; }
  .lm-tab.active + .lm-tab, .lm-tab.active { border-color: transparent; }

  .lm-field { margin-bottom: 14px; }
  .lm-label {
    display: block; font-size: 11.5px; font-weight: 600;
    color: #0a2240; letter-spacing: 0.04em; text-transform: uppercase;
    margin-bottom: 5px;
  }
  .lm-input {
    width: 100%; border: 1.5px solid #dedad2; border-radius: 4px;
    padding: 9px 12px; font-family: 'DM Sans', sans-serif;
    font-size: 13.5px; color: #1a1814; background: #fff;
    outline: none; transition: border-color 0.15s, box-shadow 0.15s;
    box-sizing: border-box;
  }
  .lm-input::placeholder { color: #c8c5bb; }
  .lm-input:focus {
    border-color: #0a2240;
    box-shadow: 0 0 0 3px rgba(10,34,64,0.07);
  }

  .lm-error {
    background: #fce4ec; border: 1px solid #ef9a9a; border-radius: 4px;
    padding: 9px 12px; font-size: 12.5px; color: #b71c1c;
    margin-bottom: 14px; display: flex; align-items: center; gap: 7px;
  }

  .lm-btn-primary {
    width: 100%; padding: 11px; background: #0a2240; color: #fff; border: none;
    border-radius: 4px; font-family: 'DM Sans', sans-serif;
    font-size: 13.5px; font-weight: 600; letter-spacing: 0.03em;
    cursor: pointer; transition: background 0.15s;
    display: flex; align-items: center; justify-content: center; gap: 8px;
  }
  .lm-btn-primary:hover { background: #14355e; }
  .lm-btn-primary:disabled { opacity: 0.6; cursor: not-allowed; }

  .lm-divider {
    text-align: center; font-size: 11.5px; color: #c8c5bb;
    margin: 16px 0; position: relative;
  }
  .lm-divider::before, .lm-divider::after {
    content: ''; position: absolute; top: 50%; width: 42%;
    height: 1px; background: #eeece6;
  }
  .lm-divider::before { left: 0; }
  .lm-divider::after { right: 0; }

  .lm-footer {
    background: #f7f6f2; border-top: 1px solid #eeece6;
    padding: 12px 28px; font-size: 11.5px; color: #8a8780;
    text-align: center;
  }
  .lm-footer a { color: #0a2240; font-weight: 600; text-decoration: none; }
  .lm-footer a:hover { text-decoration: underline; }
`;

export default function LoginModal({ onClose, onLoginSuccess }) {
  const [tab, setTab] = useState("login"); // "login" | "register"
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPass, setConfirmPass] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const reset = () => { setUsername(""); setPassword(""); setConfirmPass(""); setError(""); };

  const switchTab = (t) => { setTab(t); reset(); };

  const handleLogin = () => {
    setError("");
    if (!username || !password) { setError("Please fill in all fields."); return; }

    // Check stored users OR admin credentials
    const stored = localStorage.getItem("user_" + username);
    const isAdmin = username === "admin" && password === "admin";

    if (!isAdmin && stored !== password) {
      setError("Invalid username or password.");
      return;
    }

    setLoading(true);
    setTimeout(() => {
      login(username);           // calls your existing auth.js login()
      setLoading(false);
      onLoginSuccess?.(username);
      onClose?.();
    }, 500);
  };

  const handleRegister = () => {
    setError("");
    if (!username || !password || !confirmPass) { setError("Please fill in all fields."); return; }
    if (password !== confirmPass) { setError("Passwords do not match."); return; }
    if (password.length < 4) { setError("Password must be at least 4 characters."); return; }
    if (localStorage.getItem("user_" + username)) { setError("Username already exists."); return; }

    setLoading(true);
    setTimeout(() => {
      localStorage.setItem("user_" + username, password);
      login(username);
      setLoading(false);
      onLoginSuccess?.(username);
      onClose?.();
    }, 500);
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter") tab === "login" ? handleLogin() : handleRegister();
  };

  return (
    <>
      <style>{modalStyles}</style>
      <div className="lm-overlay" onClick={onClose}>
        <div className="lm-card" onClick={(e) => e.stopPropagation()}>

          {/* Header */}
          <div className="lm-head">
            <div>
              <div className="lm-head-logo">🏛️</div>
              <h2>{tab === "login" ? "Welcome Back" : "Create Account"}</h2>
              <p>{tab === "login" ? "Login to submit and track your complaints." : "Register to get started with IntiCivi."}</p>
            </div>
            <button className="lm-close" onClick={onClose} aria-label="Close">✕</button>
          </div>

          {/* Body */}
          <div className="lm-body">

            {/* Login / Register tabs */}
            <div className="lm-tab-row">
              <button className={`lm-tab${tab === "login" ? " active" : ""}`} onClick={() => switchTab("login")}>Login</button>
              <button className={`lm-tab${tab === "register" ? " active" : ""}`} onClick={() => switchTab("register")}>Register</button>
            </div>

            {error && <div className="lm-error">⚠️ {error}</div>}

            <div className="lm-field">
              <label className="lm-label">Username</label>
              <input
                className="lm-input" type="text"
                placeholder="Enter your username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                onKeyDown={handleKeyDown}
                autoFocus
              />
            </div>

            <div className="lm-field">
              <label className="lm-label">Password</label>
              <input
                className="lm-input" type="password"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                onKeyDown={handleKeyDown}
              />
            </div>

            {tab === "register" && (
              <div className="lm-field">
                <label className="lm-label">Confirm Password</label>
                <input
                  className="lm-input" type="password"
                  placeholder="Re-enter your password"
                  value={confirmPass}
                  onChange={(e) => setConfirmPass(e.target.value)}
                  onKeyDown={handleKeyDown}
                />
              </div>
            )}

            <button
              className="lm-btn-primary"
              onClick={tab === "login" ? handleLogin : handleRegister}
              disabled={loading}
            >
              {loading ? "Please wait…" : tab === "login" ? "Login →" : "Create Account →"}
            </button>

          </div>

          {/* Footer */}
          <div className="lm-footer">
            {tab === "login"
              ? <>Don't have an account? <a onClick={() => switchTab("register")} style={{cursor:"pointer"}}>Register here</a></>
              : <>Already registered? <a onClick={() => switchTab("login")} style={{cursor:"pointer"}}>Login here</a></>
            }
          </div>

        </div>
      </div>
    </>
  );
}