import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { isLoggedIn, logout, getUser } from "../utils/auth";
import LoginModal from "./LoginModal";

function Navbar() {
  const [loggedIn, setLoggedIn] = useState(isLoggedIn());
  const [username, setUsername] = useState(getUser?.() || "");
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();

  // Re-check auth state when localStorage changes (e.g. login from another component)
  useEffect(() => {
    const sync = () => {
      setLoggedIn(isLoggedIn());
      setUsername(getUser?.() || "");
    };
    window.addEventListener("storage", sync);
    window.addEventListener("authChange", sync); // custom event for same-tab updates
    return () => {
      window.removeEventListener("storage", sync);
      window.removeEventListener("authChange", sync);
    };
  }, []);

  const handleLogout = () => {
    logout();
    setLoggedIn(false);
    setUsername("");
    navigate("/");
  };

  const handleLoginSuccess = (user) => {
    setLoggedIn(true);
    setUsername(user);
    setShowModal(false);
  };

  return (
    <>
      <nav className="navbar navbar-expand-lg navbar-dark bg-primary">
        <div className="container">

          <Link className="navbar-brand" to="/">
            IntiCivi
          </Link>

          <button
            className="navbar-toggler"
            data-bs-toggle="collapse"
            data-bs-target="#menu"
          >
            <span className="navbar-toggler-icon"></span>
          </button>

          <div className="collapse navbar-collapse" id="menu">
            <ul className="navbar-nav ms-auto align-items-lg-center">

              <li className="nav-item">
                <Link className="nav-link" to="/">Home</Link>
              </li>

              <li className="nav-item">
                <Link className="nav-link" to="/registercomplaint">Report Issue</Link>
              </li>

              <li className="nav-item">
                <Link className="nav-link" to="/trackissue">Track Complaint</Link>
              </li>

              <li className="nav-item ms-lg-2">
                {loggedIn ? (
                  /* ── Logged-in: show avatar + username + logout ── */
                  <div className="d-flex align-items-center gap-2">
                    <span
                      style={{
                        display: "inline-flex", alignItems: "center", gap: 8,
                        background: "rgba(255,255,255,0.15)",
                        borderRadius: 100, padding: "4px 14px 4px 5px",
                        fontSize: 13, color: "#fff",
                      }}
                    >
                      <span style={{
                        width: 28, height: 28, borderRadius: "50%",
                        background: "#b8922a", color: "#0a2240",
                        fontWeight: 700, fontSize: 11,
                        display: "flex", alignItems: "center", justifyContent: "center",
                      }}>
                        {username.slice(0, 2).toUpperCase()}
                      </span>
                      {username}
                    </span>
                    <button
                      onClick={handleLogout}
                      className="btn btn-sm btn-outline-light"
                      style={{ fontSize: 12, padding: "4px 12px", borderRadius: 100 }}
                    >
                      Logout
                    </button>
                  </div>
                ) : (
                  /* ── Logged-out: Login button ── */
                  <button
                    onClick={() => setShowModal(true)}
                    className="btn btn-light btn-sm"
                    style={{ fontWeight: 600, fontSize: 13, padding: "6px 18px", borderRadius: 100 }}
                  >
                    Login
                  </button>
                )}
              </li>

            </ul>
          </div>
        </div>
      </nav>

      {showModal && (
        <LoginModal
          onClose={() => setShowModal(false)}
          onLoginSuccess={handleLoginSuccess}
        />
      )}
    </>
  );
}

export default Navbar;