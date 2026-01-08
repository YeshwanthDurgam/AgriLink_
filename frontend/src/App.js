import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { AuthProvider } from './context/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import Navbar from './components/Navbar';

import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Farms from './pages/Farms';
import FarmDetail from './pages/FarmDetail';
import CreateFarm from './pages/CreateFarm';
import Orders from './pages/Orders';
import Marketplace from './pages/Marketplace';
import Devices from './pages/Devices';

import './App.css';

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="app">
          <Navbar />
          <main className="main-content">
            <Routes>
              {/* Public Routes */}
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              
              {/* Protected Routes */}
              <Route path="/dashboard" element={
                <PrivateRoute>
                  <Dashboard />
                </PrivateRoute>
              } />
              <Route path="/farms" element={
                <PrivateRoute>
                  <Farms />
                </PrivateRoute>
              } />
              <Route path="/farms/create" element={
                <PrivateRoute>
                  <CreateFarm />
                </PrivateRoute>
              } />
              <Route path="/farms/:id" element={
                <PrivateRoute>
                  <FarmDetail />
                </PrivateRoute>
              } />
              <Route path="/orders" element={
                <PrivateRoute>
                  <Orders />
                </PrivateRoute>
              } />
              <Route path="/marketplace" element={
                <PrivateRoute>
                  <Marketplace />
                </PrivateRoute>
              } />
              <Route path="/devices" element={
                <PrivateRoute>
                  <Devices />
                </PrivateRoute>
              } />
              
              {/* Default Redirect */}
              <Route path="/" element={<Navigate to="/dashboard" replace />} />
              <Route path="*" element={<Navigate to="/dashboard" replace />} />
            </Routes>
          </main>
          <ToastContainer
            position="top-right"
            autoClose={3000}
            hideProgressBar={false}
            newestOnTop
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme="colored"
          />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
