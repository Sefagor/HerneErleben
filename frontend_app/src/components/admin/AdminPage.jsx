import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import ApiService from '../../service/ApiService';
import AdminLayout from './AdminLayout';
import './AdminPage.css'; // <--- neue CSS Datei optional

const AdminPage = () => {
    const [adminName, setAdminName] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAdminName = async () => {
            try {
                const response = await ApiService.getUserProfile();
                setAdminName(response.user.name);
            } catch (error) {
                console.error('Error fetching admin details:', error.message);
            }
        };

        fetchAdminName();
    }, []);

    return (
        <AdminLayout>
            <div className="admin-dashboard-container">
                <div className="dashboard-header">
                    <h2>Admin Dashboard</h2>
                </div>

                <div className="welcome-section">
                    <h1>👋 Welcome, <span className="admin-name">{adminName}</span></h1>
                    <p className="subtitle">Here are your available actions:</p>
                </div>

                <div className="button-grid">
                    <button onClick={() => navigate('/admin/manage-users')}>👤 Manage Users</button>
                    <button onClick={() => navigate('/admin/manage-bookings')}>📦 Manage Bookings</button>
                    <button onClick={() => navigate('/admin/booking-calendar')}>🗓️ Booking Calendar</button>
                    <button onClick={() => navigate('/admin/send-email')}>📧 Send Email</button>
                </div>
            </div>
        </AdminLayout>
    );
};

export default AdminPage;
