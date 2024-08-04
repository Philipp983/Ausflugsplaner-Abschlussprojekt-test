import { useContext, useState, useEffect } from 'react';
import axios from "axios";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import AutoComplete from "../components/AutoComplete.jsx";
import UserContext from "../UserContext.jsx";

const AusflugBearbeiten = () => {
   const { benutzer, refreshUserData } = useContext(UserContext);
   const navigate = useNavigate();
   const location = useLocation();
   const { ausflugId } = useParams();
   const [ausflug, setAusflug] = useState(null);
   const [loading, setLoading] = useState(true);
   const [error, setError] = useState(null);
   const baseUrl = import.meta.env.VITE_BASE_URL;

   const [formData, setFormData] = useState({
      titel: '',
      beschreibung: '',
      reiseziel: '',
      ausflugsdatum: '',
      enddatum: '',
      teilnehmerAnzahl: '',
      erstellerDesAusflugsId: '',
      sachenZumMitbringen: []
   });

   const [warningMessage, setWarningMessage] = useState('');
   const [existingParticipants, setExistingParticipants] = useState([]);
   const [selectedUsers, setSelectedUsers] = useState([]);

   useEffect(() => {
      if (!ausflugId) {
         setError('Keine Ausflug ID gefunden');
         setLoading(false);
         return;
      }

      // Fetch Ausflug data
      axios.get(`${baseUrl}/api/ausfluege/${ausflugId}`)
          .then(response => {
             setAusflug(response.data);
             setFormData({
                ...response.data,
                sachenZumMitbringen: response.data.sachenZumMitbringen.map(item => item.name).join(', '),
                ausflugsdatum: response.data.ausflugsdatum.slice(0, 16),
                enddatum: response.data.enddatum.slice(0, 16)
             });
             setExistingParticipants(response.data.nameDerTeilnehmer || []);
             setLoading(false);
          })
          .catch(error => {
             console.error('Error fetching Ausflug:', error);
             setError('Fehler beim Laden des Ausflugs');
             setLoading(false);
          });
   }, [ausflugId]);

   const handleChange = (e) => {
      const { name, value } = e.target;
      if (name === 'ausflugsdatum' || name === 'enddatum') {
         const formattedValue = value + ":00";
         setFormData(prevData => ({
            ...prevData,
            [name]: formattedValue
         }));
      } else {
         setFormData(prevData => ({
            ...prevData,
            [name]: value
         }));
      }
   };

   const handleSelectBenutzer = (benutzer) => {
      if (benutzer.id === +formData.erstellerDesAusflugsId) {
         setWarningMessage("Der Ersteller ist bereits Teil des Ausflugs!");
         return;
      }

      if (existingParticipants.includes(benutzer.userName) || selectedUsers.includes(benutzer.userName)) {
         setWarningMessage(`${benutzer.userName} ist schon hinzugefügt.`);
         return;
      }

      if (existingParticipants.length + selectedUsers.length >= formData.teilnehmerAnzahl - 1) {
         setWarningMessage(`Du kannst nicht mehr als ${formData.teilnehmerAnzahl} Teilnehmer (inkl. dir) hinzufügen.`);
         return;
      }

      setSelectedUsers(prevUsers => [...prevUsers, benutzer.userName]);
      setWarningMessage('');
   };

   const handleRemoveBenutzer = (userName) => {
      if (existingParticipants.includes(userName)) {
         setExistingParticipants(prevUsers => prevUsers.filter(user => user !== userName));
      } else {
         setSelectedUsers(prevUsers => prevUsers.filter(user => user !== userName));
      }
      setWarningMessage('');
   };

   const handleSubmit = (e) => {
      e.preventDefault();

      const url = `${baseUrl}/api/ausfluege/${ausflugId}`;
      const payload = {
         ...formData,
         teilnehmerListe: existingParticipants,
         sachenZumMitbringen: formData.sachenZumMitbringen.split(',').map(item => item.trim()).filter(item => item !== '')
      };

      axios.put(url, payload)
          .then(response => {
             console.log('Ausflug updated:', response.data);
             sendInvitations(ausflugId);
          })
          .catch((error) => {
             console.error('Error updating Ausflug:', error);
             setWarningMessage('Fehler beim Aktualisieren des Ausflugs');
          });
   };

   const sendInvitations = (ausflugId) => {
      if (selectedUsers.length === 0) {
         refreshUserData()  // refresh user data
             .then(() => {
                navigate(`/ausflug-seite/${ausflugId}`);
             })
             .catch(error => {
                console.error('Error refreshing user data:', error);
                navigate(`/ausflug-seite/${ausflugId}`);
             });
         return;
      }

      const url = `${baseUrl}/api/einladungen/simpel/bulk`;
      axios.post(url, selectedUsers, {
         params: { ausflugId: ausflugId }
      })
          .then(response => {
             console.log('Invitations sent:', response.data);
             refreshUserData()  // Add this line to refresh user data
                 .then(() => {
                    navigate(`/ausflug-seite/${ausflugId}`);
                 })
          })
          .catch(error => {
             console.error('Error sending invitations:', error);
             setWarningMessage('Fehler beim Senden der Einladungen');
          });
   };

   if (loading) return <div>Laden...</div>;
   if (error) return <div>Error: {error}</div>;
   if (!ausflug) return <div>Kein Ausflug gefunden</div>;

   return (
       <div className="min-h-screen flex flex-col items-center justify-center">
          <div className="blob-bg"></div>
          <div className="blob"></div>
          <div className="margin-top z-index rounded-lg p-8 w-full ausflugerstellen-max-w-md text-gray-700">
             <h1>Ausflug bearbeiten</h1>
             <form onSubmit={handleSubmit}>
                <div className="mb-4">
                   <label className="block text-sm font-medium mb-2" htmlFor="titel">Titel</label>
                   <input
                       required minLength="3" maxLength="50" type="text"
                       id="titel" name="titel"
                       value={formData.titel} onChange={handleChange}
                       placeholder="Titel muss zwischen 3 und 50 Zeichen sein"
                       className="w-full p-3 border rounded bg-gray-100 placeholder-slate-300"
                   />
                </div>
                <div className="mb-4">
                   <label className="block text-sm font-medium mb-2" htmlFor="beschreibung">Beschreibung</label>
                   <textarea
                       required minLength="10" maxLength="255"
                       id="beschreibung" name="beschreibung"
                       value={formData.beschreibung} onChange={handleChange}
                       placeholder="Beschreibung muss zwischen 10 und 255 Zeichen lang sein"
                       className="w-full p-3 border rounded bg-gray-100 placeholder-slate-300"
                       rows="3"
                   ></textarea>
                </div>
                <div className="mb-4">
                   <label className="block text-sm font-medium mb-2" htmlFor="reiseziel">Reiseziel</label>
                   <input
                       required minLength="3" maxLength="50"
                       type="text" id="reiseziel" name="reiseziel"
                       value={formData.reiseziel} onChange={handleChange}
                       placeholder="Reiseziel muss zwischen 3 und 50 Zeichen lang sein"
                       className="w-full p-3 border rounded bg-gray-100 placeholder-slate-300"
                   />
                </div>
                <div className="mb-4">
                   <label className="block text-sm font-medium mb-2" htmlFor="ausflugsdatum">Ausflugsdatum</label>
                   <input
                       required type="datetime-local"
                       id="ausflugsdatum" name="ausflugsdatum"
                       value={formData.ausflugsdatum.slice(0, 16)} onChange={handleChange}
                       className="w-full p-3 border rounded bg-gray-100 placeholder-slate-300"
                   />
                </div>
                <div className="mb-4">
                   <label className="block text-sm font-medium mb-2" htmlFor="enddatum">Enddatum</label>
                   <input
                       required type="datetime-local"
                       id="enddatum" name="enddatum"
                       value={formData.enddatum.slice(0, 16)} onChange={handleChange}
                       className="w-full p-3 border rounded bg-gray-100 placeholder-slate-300"
                   />
                </div>
                <div className="mb-4">
                   <label className="block text-sm font-medium mb-2" htmlFor="teilnehmerAnzahl">TeilnehmerAnzahl</label>
                   <input
                       required min="2" max="20"
                       type="number" id="teilnehmerAnzahl" name="teilnehmerAnzahl"
                       value={formData.teilnehmerAnzahl} onChange={handleChange}
                       placeholder="Bis maximal 20 Personen inklusive dir"
                       className="w-full p-3 border rounded bg-gray-100 placeholder-slate-300"
                   />
                </div>
                <div className="mb-4">
                   <label className="block text-sm font-medium mb-2" htmlFor="teilnehmerListe">TeilnehmerListe</label>
                   <AutoComplete
                       onSelect={handleSelectBenutzer}
                       selectedUsers={[...existingParticipants, ...selectedUsers]}
                       onRemove={handleRemoveBenutzer}
                   />
                   {warningMessage && (
                       <div
                           className="relative bg-red-100 text-red-500 p-4 m-3 rounded border border-red-500 shadow-lg font-semibold">
                          <button
                              onClick={() => setWarningMessage('')}
                              className="absolute top-0 right-0 mt-2 mr-2 bg-btn-lavender"
                          >
                             &times;
                          </button>
                          {warningMessage}
                       </div>
                   )}
                </div>
                <div className="mb-4">
                   <label className="block text-sm font-medium mb-2"
                          htmlFor="sachenZumMitbringen">SachenZumMitbringen</label>
                   <textarea
                       id="sachenZumMitbringen"
                       name="sachenZumMitbringen"
                       value={formData.sachenZumMitbringen}
                       onChange={handleChange}
                       placeholder="Die Artikel durch Kommas getrennt eingeben. Mindestens ein Artikel sollte hinzugefügt werden."
                       className="w-full p-3 border rounded bg-gray-100 placeholder-slate-300"
                   ></textarea>
                </div>
                <div className="flex justify-between">
                   <button type="button"
                           className="w-1/3 py-3 px-4 bg-btn-lavender rounded hover:bg-gray-400 transition duration-200 flex items-center justify-center"
                           onClick={() => navigate(`/ausflug-seite/${ausflug.id}`)}>
                      Verwerfen
                   </button>
                   <button type="submit"
                           className="w-1/3 py-3 px-4 bg-btn-green text-white rounded hover:bg-blue-800 transition duration-200 flex items-center justify-center">
                      Speichern
                   </button>
                </div>
             </form>
          </div>
       </div>
   );
};

export default AusflugBearbeiten;
