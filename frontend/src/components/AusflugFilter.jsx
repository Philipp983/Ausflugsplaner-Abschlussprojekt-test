import React from 'react';

const AusflugFilter = ({ onFilterChange }) => {
   const handleChange = (e) => {
      const { name, value } = e.target;
      onFilterChange(name, value);
   };

   return (
       <div className="ausflug-filter flex justify-center items-center space-x-4 mb-6">
          <input
              type="text"
              name="reiseziel"
              placeholder="Suche nach Stadt"
              onChange={handleChange}
              className="w-64 p-2 bg-gray-700 text-white placeholder-gray-400 border border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
          />
          <div className="relative">
             <select
                 name="teilnehmerAnzahl"
                 onChange={handleChange}
                 className="w-64 p-2 pr-8 bg-gray-700 text-white border border-gray-600 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 appearance-none"
             >
                <option value="">Teilnehmeranzahl</option>
                <option value="1-5">1-5 Personen</option>
                <option value="5-10">5-10 Personen</option>
                <option value="10+">Mehr als 10 Personen</option>
             </select>
             <div className="pointer-events-none absolute inset-y-0 right-0 flex items-center px-2 text-white">
                <svg className="fill-current h-4 w-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
                   <path d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z" />
                </svg>
             </div>
          </div>
       </div>
   );
};

export default AusflugFilter;