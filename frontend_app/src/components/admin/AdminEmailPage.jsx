import React, { useState } from 'react';
import ApiService from '../../service/ApiService';
import AdminLayout from './AdminLayout';

function AdminEmailPage() {
    const [subject, setSubject] = useState('');
    const [message, setMessage] = useState('');
    const [success, setSuccess] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!subject || !message) {
            setError('Please fill in both Subject and Message.');
            setTimeout(() => setError(''), 4000);
            return;
        }

        try {
            const emailContent = { subject, message };
            const response = await ApiService.sendEmailToAll(emailContent);

            if (response.statusCode === 200) {
                setSuccess('Emails sent successfully.');
                setSubject('');
                setMessage('');
                setTimeout(() => setSuccess(''), 4000);
            }
        } catch (error) {
            setError(error.response?.data?.message || error.message);
            setTimeout(() => setError(''), 5000);
        }
    };

    return (
        <AdminLayout>
            <div className="admin-email-page">
                <h2>Send Email to All Users</h2>

                {error && <p className="error-message">{error}</p>}
                {success && <p className="success-message">{success}</p>}

                <form onSubmit={handleSubmit} className="email-form">
                    <div className="form-group">
                        <label>Subject:</label>
                        <input
                            type="text"
                            value={subject}
                            onChange={(e) => setSubject(e.target.value)}
                        />
                    </div>

                    <div className="form-group">
                        <label>Message:</label>
                        <textarea
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            rows="8"
                        ></textarea>
                    </div>

                    <button type="submit" className="send-button">Send Email</button>
                </form>
            </div>
        </AdminLayout>
    );
}

export default AdminEmailPage;
