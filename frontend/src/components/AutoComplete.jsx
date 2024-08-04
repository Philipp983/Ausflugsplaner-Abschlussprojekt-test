import React, { useState, useEffect } from 'react';
import axios from 'axios';

const AutoComplete = ({ onSelect, selectedUsers = [], onRemove }) => {
   const [query, setQuery] = useState('');
   const [results, setResults] = useState([]);
    const baseUrl = import.meta.env.VITE_BASE_URL;

   useEffect(() => {
      if (query.length >= 1) {
         axios.get(`${baseUrl}/api/benutzer/search?userName=${query}`)
             .then(response => {
                const filteredResults = response.data.filter(user =>
                    !selectedUsers.includes(user.userName)
                );
                setResults(filteredResults);
             })
             .catch(error => console.error('Error fetching data:', error));
      } else {
         setResults([]);
      }
   }, [query, selectedUsers]);

   const handleSelect = (user) => {
      onSelect(user);
      setQuery('');
      setResults([]);
   };

   return (
       <div>
          <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="Benutzer hinzufügen..."
              className="w-full p-3 border rounded bg-gray-100 placeholder-slate-300"
          />
          {results.length > 0 && (
              <ul className="border rounded bg-white text-black">
                 {results.map(user => (
                     <li key={user.id} className="cursor-pointer p-2 hover:bg-gray-100 transition duration-300"
                         onClick={() => handleSelect(user)}>
                        {user.userName}
                     </li>
                 ))}
              </ul>
          )}
          {selectedUsers.length > 0 && (
              <div className="mt-4">
                 <h3 className="font-bold mb-2">Ausgewählte Benutzer:</h3>
                 <ul>
                    {selectedUsers.map(userName => (
                        <li key={userName} className="flex justify-between items-center p-2 bg-gray-100 mb-2 rounded">
                           {userName}
                           <button
                               onClick={() => onRemove(userName)}
                               className="bg-red-500 text-white px-2 py-1 rounded hover:bg-red-600 transition duration-300"
                           >
                              Entfernen
                           </button>
                        </li>
                    ))}
                 </ul>
              </div>
          )}
       </div>
   );
};

export default AutoComplete;
