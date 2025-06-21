import React, {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import ApiService from '../../service/ApiService';
import NotificationBadge from './NotificationBadge';
import AdminLayout from './AdminLayout'


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
               <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                            <h2>Admin Dashboard</h2>
                            <NotificationBadge />
                        </div>
        <div className="admin-page">
            <h1 className="welcome-message">Welcome, {adminName}</h1>
            <div className="admin-actions">
                <button className="admin-button" onClick={() => navigate('/admin/manage-users')}>
                    Manage Users
                </button>
                <button className="admin-button" onClick={() => navigate('/admin/manage-bookings')}>
                    Manage Bookings
                </button>
                <button className="admin-button" onClick={() => navigate('/admin/add-booking')}>Add Booking (Manual)</button>
                <button className="admin-button" onClick={() => navigate('/admin/booking-calendar')}>Booking Calendar</button>

            </div>
        </div>
        </AdminLayout>
    );
}

export default AdminPage;
