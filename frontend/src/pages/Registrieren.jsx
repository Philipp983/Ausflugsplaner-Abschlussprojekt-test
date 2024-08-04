import { FaArrowRightFromBracket } from "react-icons/fa6";
import {Link, useNavigate} from 'react-router-dom';
import axios from "axios";
import { useState } from "react";
import Popup from "../components/Popup.jsx";

const Registrieren = () => {
  const [showAnmeldenPopup, setShowAnmeldenPopup] = useState(false);
  const profilBild = "https://cdn.pixabay.com/photo/2018/11/13/21/43/avatar-3814049_960_720.png"
  const baseUrl = import.meta.env.VITE_BASE_URL;
  const [formData, setFormData] = useState({
    userName: "",
    userEmail: "",
    passwort: "",
  });

  const handleChange = (e) => {
    const { id, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [id]: value,
    }));
  };

  const handlePopupConfirm = () => {
    setShowAnmeldenPopup(false);
    navigate('/login');
  };
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const { userName, userEmail, passwort } = formData;

    console.log(userName, userEmail, passwort);

    axios
        .post(`${baseUrl}/api/benutzer`, {
          userName,
          userEmail,
          passwort,
        })
        .then((result) => {
          console.log(result);
          setShowAnmeldenPopup(true);
        })
        .catch((err) => {
          console.log(err);
          alert("Die Registrierung war nicht erfolgreich. Bitte versuch es mit einer anderen E-Mail");
          return;
        });
  };

  return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="p-l-1rem registrieren z-index shadow container mx-auto px-4 rounded-lg p-8 w-full max-w-md">
          <h2 className="mb-8 text-center">Registrieren</h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label className="block text-sm font-medium mb-2" htmlFor="userName">Name</label>
              <input
                  type="text"
                  id="userName"
                  placeholder="name"
                  value={formData.userName}
                  onChange={handleChange}
                  className="w-full p-3 border rounded bg-gray-100"
              />
            </div>
            <div className="mb-4">
              <label className="block text-sm font-medium mb-2" htmlFor="userEmail">E-Mail</label>
              <input
                  type="email"
                  id="userEmail"
                  placeholder="beispiel@gmail.com"
                  value={formData.userEmail}
                  onChange={handleChange}
                  className="w-full p-3 border rounded bg-gray-100"
              />
            </div>
            <div className="mb-4 relative">
              <label className="block text-sm font-medium mb-2" htmlFor="passwort">Passwort</label>
              <input
                  type="password"
                  id="passwort"
                  placeholder="mindestens 8 Zeichen"
                  value={formData.passwort}
                  onChange={handleChange}
                  className="w-full p-3 border rounded bg-gray-100"
              />
            </div>
            <div className="mb-6 relative">
              <label className="block text-sm font-medium mb-2" htmlFor="confirmPassword">Passwort bestätigen</label>
              <input
                  type="password"
                  id="confirmPassword"
                  placeholder="passwort erneut eingeben"
                  className="w-full p-3 border rounded bg-gray-100"
              />
            </div>
            <button
                className="w-full py-3 px-4 bg-custom-blue text-white rounded hover:bg-blue-800 transition duration-200"
                type="submit">
              <FaArrowRightFromBracket className="h-5 w-5 inline-block mr-2"/>
              Registrieren
            </button>
          </form>
          <p className="mt-4 text-center text-sm">
            Hast du bereits ein Konto? <Link to="/login" className="text-blue-700 hover:underline">einloggen</Link>
          </p>
        </div>
        {showAnmeldenPopup && (
            <Popup
                description="Du hast dich erfolgreich registriert. Bitte melde dich an."
                onCancel={() => setShowAnmeldenPopup(false)}
                onConfirm={handlePopupConfirm}
                cancelText="Abbrechen"
                confirmText="Anmelden"
            />
        )}
      </div>
  );
};

export default Registrieren;
