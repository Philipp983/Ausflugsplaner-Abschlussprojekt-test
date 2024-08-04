import {Routes, Route, BrowserRouter} from 'react-router-dom';
import Home from './pages/Home';
import Registrieren from './pages/Registrieren.jsx';
import Anmeldung from './pages/Anmeldung.jsx';
import About from './pages/About';
import AusflugErstellen from './pages/AusflugErstellen';
import {UserProvider} from "./UserContext.jsx";
import MainLayout from "./layout/Layout.jsx";
import BenutzerSeite from "./pages/BenutzerSeite.jsx";
import AusflugSeite from "./pages/AusflugSeite.jsx";
import BenutzerBearbeiten from './pages/BenutzerBearbeiten.jsx';
import BenutzerAllgemeineSeite from "./pages/BenutzerAllgemeineSeite.jsx";
import AusflugBearbeiten from "./pages/AusflugBearbeiten.jsx";
import Einladungen from "./components/Einladungen.jsx";


const App = () => {
    return (
        <BrowserRouter>
            <div className="App">
                <UserProvider>
                    <Routes>
                        <Route path="/" element={<MainLayout/>}>
                            {" "}
                            <Route index element={<Home/>}/>
                            <Route path="/signup" element={<Registrieren/>}/>
                            <Route path="/login" element={<Anmeldung/>}/>
                            <Route path="/about" element={<About/>}/>
                            <Route path="/benutzer-seite" element={<BenutzerSeite/>}/>
                            <Route path="/benutzer-seite/:id" element={<BenutzerAllgemeineSeite/>}/>
                            <Route path="/benutzer-bearbeiten" element={<BenutzerBearbeiten />} />
                            <Route path="/benutzer/:id/einladungen" element={<Einladungen />} />
                            <Route path="/ausflug-erstellen" element={<AusflugErstellen/>}/>
                            <Route path="/ausflug-seite/:id" element={<AusflugSeite/>} />
                            <Route path="/ausflug-bearbeiten/:ausflugId" element={<AusflugBearbeiten />} />
                            <Route path="/invitations" element={<Einladungen />} />
                            {/*<Route path="*" element={<NichtGefunden />} />*/}
                        </Route>
                    </Routes>
                </UserProvider>
            </div>
        </BrowserRouter>
    );
};

export default App;
