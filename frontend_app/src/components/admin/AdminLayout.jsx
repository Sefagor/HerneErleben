import React from 'react';
import AdminSidebar from './AdminSidebar';
import '../../service/AdminLayout.css'; // Styling → poste ich dir gleich

function AdminLayout({ children }) {
    return (
        <div className="admin-layout">
            <AdminSidebar />
            <div className="admin-content">
                {children}
            </div>
        </div>
    );
}

export default AdminLayout;
