import React, { useState, useEffect, useRef } from 'react';
import AusflugCardListe from "../components/AusflugCardListe";
import Pagination from "../components/Pagination";
import AusflugFilter from "../components/AusflugFilter";
import TriangleSpinner from "../utils/TriangleSpinner.jsx";


const Home = () => {
  const [ausfluege, setAusfluege] = useState([]);
  const [filteredAusfluege, setFilteredAusfluege] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [ausfluegePerPage] = useState(6);
  const [filters, setFilters] = useState({ reiseziel: '', teilnehmerAnzahl: '' });
  const [loading, setLoading] = useState(true); // Add loading state
  const baseUrl = import.meta.env.VITE_BASE_URL;
  const paginationAnchorRef = useRef(null);

  useEffect(() => {
    fetchAusfluege();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [ausfluege, filters]);

  const indexOfLastAusflug = currentPage * ausfluegePerPage;
  const indexOfFirstAusflug = indexOfLastAusflug - ausfluegePerPage;
  const currentAusfluege = filteredAusfluege.slice(indexOfFirstAusflug, indexOfLastAusflug);

  const paginate = (pageNumber) => {
    setCurrentPage(pageNumber);
    if (paginationAnchorRef.current) {
      paginationAnchorRef.current.scrollIntoView({ behavior: 'smooth' });
    }
  };

  const fetchAusfluege = async () => {
    setLoading(true); // Start loading
    try {
      const response = await fetch(`${baseUrl}/api/ausfluege`);
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      const data = await response.json();
      setAusfluege(data);
      setFilteredAusfluege(data);
    } catch (error) {
      console.error('Error fetching Ausflüge:', error);
    } finally {
      setLoading(false); // Stop loading
    }
  };

  const handleFilterChange = (name, value) => {
    setFilters(prevFilters => ({ ...prevFilters, [name]: value }));
  };

  const applyFilters = () => {
    let filtered = ausfluege;

    if (filters.reiseziel) {
      filtered = filtered.filter(ausflug =>
          ausflug.reiseziel.toLowerCase().includes(filters.reiseziel.toLowerCase())
      );
    }

    if (filters.teilnehmerAnzahl) {
      filtered = filtered.filter(ausflug => {
        const count = ausflug.teilnehmerAnzahl;
        switch (filters.teilnehmerAnzahl) {
          case '1-5':
            return count >= 1 && count <= 5;
          case '5-10':
            return count > 5 && count <= 10;
          case '10+':
            return count > 10;
          default:
            return true;
        }
      });
    }

    setFilteredAusfluege(filtered);
    setCurrentPage(1);
  };

  return (
      <div className="home min-h-screen flex flex-col items-center justify-center text-center">
        <div className="z-index container mx-auto px-4 flex flex-col items-center">
          {/*<img src="/public/logo.png" alt="Illustration" className="main-pic"/>*/}
          <h1 className="t mb-4">Ausflugsplaner Test 123456789</h1>
          <AusflugFilter onFilterChange={handleFilterChange} />
          <div className="ausflug-liste-home">
            <div ref={paginationAnchorRef} className="pagination-anchor"></div>
            {loading ? (
                <TriangleSpinner visible={true} />
            ) : (
                <AusflugCardListe ausfluege={currentAusfluege} baseUrl={baseUrl}/>
            )}
          </div>

          <Pagination
              ausfluegePerPage={ausfluegePerPage}
              totalAusfluege={filteredAusfluege.length}
              paginate={paginate}
              currentPage={currentPage}
          />
        </div>
      </div>
  );
};

export default Home;
