import { FaArrowRightFromBracket } from "react-icons/fa6";
import {Link, useNavigate} from "react-router-dom";
import { useContext, useState } from "react";
import axios from "axios";
import UserContext from "../UserContext.jsx";

const Anmeldung = () => {
  const { benutzerHandle } = useContext(UserContext);
  const baseUrl = import.meta.env.VITE_BASE_URL;

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const handleChange = (e) => {
    const { id, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [id]: value,
    }));
  };

  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();

    const { email, password } = formData;

    axios
        .post(`${baseUrl}/api/benutzer/login`, {
          userEmail: email,
          passwort: password,
        })
        .then((response) => {
          benutzerHandle(response.data);
          navigate('/');
        })
        .catch((err) => {
          console.log(err);
          alert("E-Mail oder Passwort sind falsch. Versuche es erneut");
        });
  };

  return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="anmeldung z-index shadow custom-shadow container mx-auto px-4 rounded-lg p-8 shadow-md w-full max-w-md p-l-1rem">
          <h2 className="text-2xl font-bold mb-8 text-center">Einloggen</h2>
          <form onSubmit={handleSubmit}>
            <div className="mb-4">
              <label className="block text-sm font-medium mb-2" htmlFor="email">
                E-Mail
              </label>
              <input
                  type="email"
                  id="email"
                  value={formData.email}
                  onChange={handleChange}
                  placeholder="beispiel@mail.de"
                  className="w-full p-3 border rounded bg-gray-100"
              />
            </div>
            <div className="mb-6 relative">
              <label className="block text-sm font-medium mb-2" htmlFor="password">
                Passwort
              </label>
              <input
                  type="password"
                  id="password"
                  value={formData.password}
                  onChange={handleChange}
                  placeholder="mindestens 8 Zeichen"
                  className="w-full p-3 border rounded bg-gray-100"
              />
            </div>
            <button
                className="w-full py-3 px-4 bg-custom-blue text-white rounded hover:bg-blue-800 transition duration-200"
                type="submit"
            >
              <FaArrowRightFromBracket className="h-5 w-5 inline-block mr-2"/>
              Einloggen
            </button>
          </form>
          <p className="mt-4 text-center text-sm">
            Noch kein Konto?{" "}
            <Link to="/signup" className="gold-color hover:underline">
              Registrieren
            </Link>
          </p>
        </div>
      </div>
  );
};

export default Anmeldung;
