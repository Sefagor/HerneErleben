// frontend_app/src/common/EventSearch.jsx
import React, {useEffect, useState} from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import ApiService from '../../../service/ApiService';
import styles from './EventSearch.module.css';
import {FaCalendarAlt, FaMapMarkerAlt, FaTags} from 'react-icons/fa';

const EventSearch = ({handleSearchResult, inline = false}) => {
    const [location, setLocation] = useState('');
    const [category, setCategory] = useState('');
    const [dateType, setDateType] = useState('today');
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [categories, setCategories] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        ApiService.getEventCategories()
            .then(list => setCategories(list))
            .catch(err => console.error(err));
    }, []);

    const showError = (msg, timeout = 5000) => {
        setError(msg);
        setTimeout(() => setError(''), timeout);
    };

    const onSearch = async () => {
        if (!location || !category) {
            return showError('Bitte wählen Sie Datum und Kategorie.');
        }

        let from, to;
        const today = new Date();
        switch (dateType) {
            case 'today':
                from = to = today.toISOString().slice(0, 10);
                break;
            case 'weekend': {
                const day = today.getDay();
                const sat = new Date(today);
                sat.setDate(today.getDate() + ((6 - day + 7) % 7));
                const sun = new Date(today);
                sun.setDate(today.getDate() + ((7 - day + 7) % 7));
                from = sat.toISOString().slice(0, 10);
                to = sun.toISOString().slice(0, 10);
                break;
            }
            case 'custom':
                if (!startDate || !endDate) {
                    return showError('Bitte wählen Sie das Zeitraum.');
                }
                from = startDate.toISOString().slice(0, 10);
                to = endDate.toISOString().slice(0, 10);
                break;
            default:
                return;
        }

        try {
            const params = {location, category, from, to};
            const {statusCode, eventList} = await ApiService.getEventsByParams(params);
            if (statusCode === 200) {
                if (!eventList.length) return showError('Das Veranstalltung konnte nicht gefunden werden.');
                handleSearchResult(eventList);
            }
        } catch {
            showError('Ein Unbekannter Fehler passiert.');
        }
    };

    return (
        <div className={`${styles.searchRow} ${inline ? styles.inline : styles.absolute}`}>
            {/* Ort */}
            <div className={styles.item}>
                <div className={styles.label}>Ort</div>
                <div className={styles.control}>
                    <FaMapMarkerAlt className={styles.icon}/>
                    <input
                        type="text"
                        value={location}
                        onChange={e => setLocation(e.target.value)}
                        placeholder="Stadt"
                        className={styles.input}
                    />
                </div>
            </div>

            {/* Kategorie */}
            <div className={styles.item}>
                <div className={styles.label}>Kategorie</div>
                <div className={styles.control}>
                    <FaTags className={styles.icon}/>
                    <select
                        value={category}
                        onChange={e => setCategory(e.target.value)}
                        className={styles.select}
                    >
                        <option value="" disabled>Wählen Sie</option>
                        {categories.map(cat => (
                            <option key={cat} value={cat}>{cat}</option>
                        ))}
                    </select>
                </div>
            </div>

            {/* Genauer Zeitraum */}
            <div className={styles.item}>
                <div className={styles.label}>Zeitraum</div>
                <div className={styles.control}>
                    <FaCalendarAlt className={styles.icon}/>
                    <select
                        value={dateType}
                        onChange={e => setDateType(e.target.value)}
                        className={styles.select}
                    >
                        <option value="today">Heute</option>
                        <option value="weekend">Dieses Wochenende</option>
                        <option value="custom">Genauer Zeitraum</option>
                    </select>
                </div>
                {dateType === 'custom' && (
                    <div className={styles.inlinePicker}>
                        <DatePicker
                            selectsRange
                            startDate={startDate}
                            endDate={endDate}
                            onChange={([s, e]) => {
                                setStartDate(s);
                                setEndDate(e);
                            }}
                            inline
                        />
                    </div>
                )}
            </div>

            {/* Search Button */}
            <button className={styles.button} onClick={onSearch}>
                Suchen
            </button>
        </div>
    );
};

export default EventSearch;
