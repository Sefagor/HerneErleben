import {FaBook, FaRocket, FaSmile} from 'react-icons/fa';
import Banner from '../Banner/Banner';
import KommendeVeranstalltungen from '../KommendeVeranstalltungen/KommendeVeranstalltungen';
import styles from "./HomePage.module.css";

const HomePage = () => {
    return (
        <div className={styles.home}>

            <div>
                <Banner/>
            </div>

            <div className={styles.divider}>
                <div className={styles.card}>
                    <FaSmile className={styles.icon}/>
                    <h3>Arbeiten &amp; Spaß</h3>
                    <p>Lass den Alltagsstress hinter dir und entdecke spannende Events.</p>
                </div>
                <div className={styles.card}>
                    <FaBook className={styles.icon}/>
                    <h3>Lernen</h3>
                    <p>Erweitere dein Wissen mit interessanten Workshops und Kursen.</p>
                </div>
                <div className={styles.card}>
                    <FaRocket className={styles.icon}/>
                    <h3>Weiterentwickeln</h3>
                    <p>Mach den nächsten Schritt auf deinem Weg zum Erfolg.</p>
                </div>
            </div>

            <KommendeVeranstalltungen/>
        </div>
    );
};

export default HomePage;
