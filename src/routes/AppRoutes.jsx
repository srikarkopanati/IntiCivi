import { Routes, Route } from "react-router-dom"

import LandingPage from "../pages/LandingPage"
import MainLayout from "../layouts/MainLayout"
import RegisterComplaint from "../pages/RegisterComplaint";

function AppRoutes() {
  return (
    <Routes>

      <Route element={<MainLayout />}>
        <Route path="/" element={<LandingPage />} />
        <Route path="/report" element={<RegisterComplaint />} />
      </Route>

    </Routes>
  )
}

export default AppRoutes