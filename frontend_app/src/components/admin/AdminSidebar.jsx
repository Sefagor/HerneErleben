import React from 'react';
import {NavLink} from 'react-router-dom';
import '../../service/AdminSidebar.css';

function AdminSidebar() {
    return (
        <div className="admin-sidebar">
            <h3 className="sidebar-title">Admin Panel</h3>
            <nav className="sidebar-nav">
                <NavLink to="/admin" className={({isActive}) => isActive ? 'active' : ''}>
                    Dashboard
                </NavLink>
                <NavLink to="/admin/manage-users" className={({isActive}) => isActive ? 'active' : ''}>
                    Manage Users
                </NavLink>
                <NavLink to="/admin/manage-bookings" className={({isActive}) => isActive ? 'active' : ''}>
                    Manage Bookings
                </NavLink>
                <NavLink to="/admin/add-booking" className={({isActive}) => isActive ? 'active' : ''}>
                    Add Booking
                </NavLink>
                <NavLink to="/admin/booking-calendar" className={({isActive}) => isActive ? 'active' : ''}>
                    Booking Calendar
                </NavLink>
                <NavLink to="/admin/send-email" className={({isActive}) => isActive ? 'active' : ''}>
                    Send Email
                </NavLink>

            </nav>
        </div>
    );
}

export default AdminSidebar;
