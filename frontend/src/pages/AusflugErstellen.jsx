import {useContext, useState, useEffect} from 'react';
import axios from "axios";
import {useNavigate} from "react-router-dom";
import AutoComplete from "../components/AutoComplete.jsx";
import UserContext from "../UserContext.jsx";

const AusflugErstellen = () => {
   const { benutzer, refreshUserData } = useContext(UserContext);
   const navigate = useNavigate();
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
   const [selectedUsers, setSelectedUsers] = useState([]);

   useEffect(() => {
      setFormData((prevFormData) => ({
         ...prevFormData,
         erstellerDesAusflugsId: benutzer.id,
      }));
   }, [benutzer.id]);

   const handleChange = (e) => {
      const { name, value } = e.target;
      if (name === 'ausflugsdatum' || name === 'enddatum') {
         const formattedValue = value + ":00";
         setFormData({
            ...formData,
            [name]: formattedValue
         });
      } else {
         setFormData({
            ...formData,
            [name]: value
         });
      }
   };

   const handleSelectBenutzer = (benutzer) => {
      if (benutzer.id === +formData.erstellerDesAusflugsId) {
         setWarningMessage("Du wurdest automatisch in die Liste aufgenommen!");
         return;
      }

      if (selectedUsers.includes(benutzer.userName)) {
         setWarningMessage(`${benutzer.userName} ist schon hinzugefügt.`);
         return;
      }

      if (selectedUsers.length >= formData.teilnehmerAnzahl - 1) {
         setWarningMessage(`Du kannst nicht mehr als ${formData.teilnehmerAnzahl} Teilnehmer (inkl. dir) hinzufügen.`);
         return;
      }

      setSelectedUsers(prevUsers => [...prevUsers, benutzer.userName]);
      setWarningMessage('');
   };

   const handleRemoveBenutzer = (userName) => {
      setSelectedUsers(prevUsers => prevUsers.filter(user => user !== userName));
      setWarningMessage('');
   };

   const handleSubmit = (e) => {
      e.preventDefault();

      const url = `${baseUrl}/api/ausfluege/${benutzer.id}`;
      const payload = {
         ...formData,
         teilnehmerListe: [benutzer.userName],
         sachenZumMitbringen: formData.sachenZumMitbringen.split(',').map(item => item.trim()).filter(item => item !== '')
      };

      axios.post(url, payload)
          .then(response => {
             console.log('Success:', response.data);
             const ausflug = response.data;
             sendInvitations(ausflug.id);
          })
          .catch((error) => {
             console.error('Error:', error);
             setWarningMessage('Fehler beim Erstellen des Ausflugs');
          });
   };

   const sendInvitations = (ausflugId) => {
      const url = `${baseUrl}/api/einladungen/simpel/bulk`;
      axios.post(url, selectedUsers, {
         params: { ausflugId: ausflugId }
      })
          .then(response => {
             console.log('Invitations sent:', response.data);
             refreshUserData()
                 .then(() => {
                navigate(`/ausflug-seite/${ausflugId}`);
             })
          })
          .catch(error => {
             console.error('Error sending invitations:', error);
             setWarningMessage('Fehler beim Senden der Einladungen');
          });
   };

   return (
       <div className="min-h-screen flex flex-col items-center justify-center">
          <div className="ausflugerstellen margin-top position-relative z-index rounded-lg p-8 w-full ausflugerstellen-max-w-md text-gray-700">
             <h1>Ausflug erstellen</h1>
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
                   <label className="block text-sm font-medium mb-2" htmlFor="teilnehmerAnzahl">Teilnehmeranzahl</label>
                   <input
                       required min="2" max="20"
                       type="number" id="teilnehmerAnzahl" name="teilnehmerAnzahl"
                       value={formData.teilnehmerAnzahl} onChange={handleChange}
                       placeholder="Bis maximal 20 Personen inklusive dir"
                       className="w-full p-3 border rounded bg-gray-100 placeholder-slate-300"
                   />
                </div>
                <div className="mb-4">
                   <label className="block text-sm font-medium mb-2" htmlFor="teilnehmerListe">Teilnehmerliste</label>
                   <AutoComplete
                       onSelect={handleSelectBenutzer}
                       selectedUsers={selectedUsers}
                       onRemove={handleRemoveBenutzer}
                   />
                   {warningMessage && (
                       <div
                           className="relative bg-red-100 text-red-500 p-4 m-3 rounded border border-red-500 shadow-lg font-semibold">
                          <button
                              onClick={() => setWarningMessage('')}
                              className="absolute top-0 right-0 mt-2 mr-2 text-red-500 hover:text-red-700"
                          >
                             &times;
                          </button>
                          {warningMessage}
                       </div>
                   )}
                </div>

                <div className="mb-4">
                   <label className="block text-sm font-medium mb-2"
                          htmlFor="sachenZumMitbringen">Mitbringsel</label>
                   <textarea
                       id="sachenZumMitbringen"
                       name="sachenZumMitbringen"
                       value={formData.sachenZumMitbringen}
                       onChange={handleChange}
                       placeholder="Mitbringsel durch Kommas getrennt angeben. Mindestens ein Artikel muss hinzugefügt werden."
                       className="w-full p-3 border rounded bg-gray-100 placeholder-slate-300"
                   ></textarea>
                </div>

                <div className="flex justify-between">
                   <button type="button"
                           className="w-1/3 py-3 px-4 bg-btn-lavender text-white rounded hover:bg-gray-400 transition duration-200 flex items-center justify-center">
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

export default AusflugErstellen;
