import React, {useEffect, useRef, useState} from 'react';
import ApiService from '../../../service/ApiService';
import styles from './EventSearch.module.css';

const EventSearch = ({handleSearchResult, inline = false, events = []}) => {
    const [location, setLocation] = useState('');
    const [categorySearch, setCategorySearch] = useState('');
    const [category, setCategory] = useState('');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const [allCategories, setAllCategories] = useState([]);
    const [filteredCategories, setFilteredCategories] = useState([]);
    const [showCat, setShowCat] = useState(false);
    const catRef = useRef(null);

    useEffect(() => {
        ApiService.getEventCategories()
            .then(cats => {
                const names = cats.map(c => c.name);
                const uniq = Array.from(new Set(names));
                setAllCategories(uniq);
                setFilteredCategories(uniq);
            })
            .catch(console.error);
    }, []);

    useEffect(() => {
        const handler = e => {
            if (catRef.current && !catRef.current.contains(e.target)) {
                setShowCat(false);
            }
        };
        document.addEventListener('mousedown', handler);
        return () => document.removeEventListener('mousedown', handler);
    }, []);

    const handleCategoryInput = e => {
        const val = e.target.value;
        setCategorySearch(val);
        setCategory(val);
        setFilteredCategories(allCategories.filter(name => name.toLowerCase().includes(val.toLowerCase())));
        setShowCat(true);
        setErrorMessage('');
    };

    const selectCategory = name => {
        setCategory(name);
        setCategorySearch(name);
        setShowCat(false);
        setErrorMessage('');
    };

    const isSearchEnabled = location && category && fromDate && toDate;

    const onSearch = e => {
        e.preventDefault();
        console.log('isSearchEnabled:', isSearchEnabled, {location, category, fromDate, toDate});
        setErrorMessage('');

        if (!isSearchEnabled) {
            setErrorMessage('Bitte alle Felder ausfüllen');
            return;
        }

        if (inline) {
            const result = events.filter(ev => {
                const evDate = new Date(ev.eventDate);
                const from = new Date(fromDate);
                const to = new Date(toDate);
                const okDate = evDate >= from && evDate <= to;
                const city = ev.eventLocation?.city || '';
                const okLoc = city.toLowerCase().includes(location.toLowerCase());
                const okCat = ev.categories?.some(c => c.name === category);
                return okDate && okLoc && okCat;
            });
            handleSearchResult(result);
        } else {
            ApiService.searchEvents({location, category, from: fromDate, to: toDate})
                .then(res => {
                    const list = Array.isArray(res) ? res : res.eventList || [];
                    handleSearchResult(list);
                })
                .catch(err => console.error(err));
        }
    };

    return (
        <form className={styles.searchForm} onSubmit={onSearch}>
            <div className={styles.inputGroup}>
                <p className={styles.label}>Ort (z.B. München):</p>
                <input
                    type="text"
                    placeholder="Ort"
                    value={location}
                    onChange={e => {
                        setLocation(e.target.value);
                        setErrorMessage('');
                    }}
                />
            </div>

            <div className={styles.inputGroup}>
                <p className={styles.label}>Kategorie (z.B. Musik):</p>
                <div className={styles.categoryContainer} ref={catRef}>
                    <input
                        type="text"
                        placeholder="Kategorie"
                        value={categorySearch}
                        onChange={handleCategoryInput}
                        onFocus={() => setShowCat(true)}
                        autoComplete="off"
                    />
                    {showCat && (
                        <ul className={styles.dropdownList}>
                            {filteredCategories.map(name => (
                                <li
                                    key={name}
                                    className={styles.dropdownItem}
                                    onClick={() => selectCategory(name)}
                                >
                                    {name}
                                </li>
                            ))}
                        </ul>
                    )}
                </div>
            </div>

            <div className={styles.dateGroup}>
                <p className={styles.label}>Von (Startdatum):</p>
                <input
                    type="date"
                    aria-label="Von (Startdatum)"
                    value={fromDate}
                    onChange={e => {
                        setFromDate(e.target.value);
                        setErrorMessage('');
                    }}
                />
            </div>
            <div className={styles.dateGroup}>
                <p className={styles.label}>Bis (Enddatum):</p>
                <input
                    type="date"
                    aria-label="Bis (Enddatum)"
                    value={toDate}
                    onChange={e => {
                        setToDate(e.target.value);
                        setErrorMessage('');
                    }}
                />
            </div>

            <div className={styles.buttonGroup}>
                <button type="submit">Suchen</button>
                <p className={styles.error}>{errorMessage}</p>
            </div>
        </form>
    );
};

export default EventSearch;
