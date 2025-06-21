
import React, { useState, useEffect } from 'react';
import ApiService from '../../service/ApiService';
import { useNavigate } from 'react-router-dom';

function EditProfilePage() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        phoneNumber: ''
    });

    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');

    // Load user profile on mount
    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                const profile = await ApiService.getUserProfile();
                setFormData({
                    name: profile.name || '',
                    email: profile.email || '',
                    phoneNumber: profile.phoneNumber || '',
                    password: '' // password leer lassen → User muss neu setzen wenn gewollt
                });
            } catch (error) {
                setErrorMessage('Failed to load profile.');
                setTimeout(() => setErrorMessage(''), 5000);
            }
        };

        fetchUserProfile();
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const validateForm = () => {
        const { name, email, phoneNumber } = formData;
        if (!name || !email || !phoneNumber) {
            return false;
        }
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!validateForm()) {
            setErrorMessage('Please fill all required fields (Name, Email, Phone Number).');
            setTimeout(() => setErrorMessage(''), 5000);
            return;
        }
        try {
            // Call update profile API
            const response = await ApiService.updateUserProfile(formData);

            if (response.statusCode === 200) {
                setSuccessMessage('Profile updated successfully');
                setTimeout(() => {
                    setSuccessMessage('');
                    navigate('/'); // oder auf Profilseite zurück
                }, 3000);
            }
        } catch (error) {
            setErrorMessage(error.response?.data?.message || error.message);
            setTimeout(() => setErrorMessage(''), 5000);
        }
    };

    return (
        <div className="auth-container">
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            {successMessage && <p className="success-message">{successMessage}</p>}
            <h2>Edit Profile</h2>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label>Name:</label>
                    <input type="text" name="name" value={formData.name} onChange={handleInputChange} required />
                </div>
                <div className="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value={formData.email} onChange={handleInputChange} required />
                </div>
                <div className="form-group">
                    <label>Phone Number:</label>
                    <input type="text" name="phoneNumber" value={formData.phoneNumber} onChange={handleInputChange} required />
                </div>
                <div className="form-group">
                    <label>New Password (optional):</label>
                    <input type="password" name="password" value={formData.password} onChange={handleInputChange} />
                </div>

               <div className="form-actions">
                   <button type="submit" className="save-button">Save Changes</button>
                   <button type="button" className="cancel-button" onClick={() => navigate('/profile')}>
                       Cancel
                   </button>
               </div>

            </form>
        </div>
    );
}

export default EditProfilePage;