import { Routes, Route, Navigate } from "react-router-dom"

import LandingPage from "../pages/LandingPage"
import MainLayout from "../layouts/MainLayout"
import RegisterComplaint from "../pages/RegisterComplaint"
import Login from "../pages/Login"
import Register from "../pages/Register"

// Protected route — redirects to /login if no token
function ProtectedRoute({ children }) {
  const token = localStorage.getItem("token");
  return token ? children : <Navigate to="/login" />;
}

function AppRoutes() {
  return (
    <Routes>

      <Route element={<MainLayout />}>
        <Route path="/" element={<LandingPage />} />
        <Route
          path="/registercomplaint"
          element={
            <ProtectedRoute>
              <RegisterComplaint />
            </ProtectedRoute>
          }
        />
      </Route>

      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route
        path="/report"
        element={
          <ProtectedRoute>
            <RegisterComplaint />
          </ProtectedRoute>
        }
      />

    </Routes>
  )
}

export default AppRoutes