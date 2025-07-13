import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import ApiService from '../../service/ApiService';
import '../../service/ManageUsersPage.css'
import AdminLayout from '../admin/AdminLayout';

function ManageUsersPage() {
    const [users, setUsers] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await ApiService.getAllUsers();
            setUsers(response.userList || response); // fallback falls API nur Array liefert
        } catch (error) {
            setErrorMessage(error.response?.data?.message || error.message);
            setTimeout(() => setErrorMessage(''), 5000);
        }
    };

    const handleDelete = async (userId) => {
        if (!window.confirm('Do you really want to delete this user?')) return;

        try {
            const response = await ApiService.deleteUser(userId);
            if (response.statusCode === 200) {
                setSuccessMessage('User deleted successfully');
                fetchUsers(); // Reload user list
                setTimeout(() => setSuccessMessage(''), 5000);
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || error.message);
            setTimeout(() => setErrorMessage(''), 5000);
        }
    };

    return (
        <AdminLayout>
            <div className="manage-users-container">
                {errorMessage && <p className="error-message">{errorMessage}</p>}
                {successMessage && <p className="success-message">{successMessage}</p>}
                <h2>Manage Users</h2>

                <button className="add-user-button" onClick={() => navigate('/admin/add-user')}>
                    Add New User
                </button>

                <table className="users-table">
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Role</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {users.length === 0 ? (
                        <tr>
                            <td colSpan="5">No users found.</td>
                        </tr>
                    ) : (
                        users.map(user => (
                            <tr key={user.id}>
                                <td>{user.name}</td>
                                <td>{user.email}</td>
                                <td>{user.phoneNumber}</td>
                                <td>{user.role}</td>
                                <td>
                                    <button onClick={() => navigate(`/admin/edit-user/${user.id}`)}>Edit</button>
                                    <button onClick={() => handleDelete(user.id)}>Delete</button>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>
        </AdminLayout>
    );
}

export default ManageUsersPage;
