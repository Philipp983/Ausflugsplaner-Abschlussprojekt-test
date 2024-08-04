
import '../styles/ausflugCardListe.css';
import AusflugCard from "./AusflugCard.jsx";


const CardList = ({ausfluege, baseUrl}) => {


    return (
        <div className="card-list">
            {ausfluege.map((ausflug, index) => (
                <AusflugCard
                    key={index}
                    ausflug={ausflug}
                    baseUrl={baseUrl}
                />
            ))}
        </div>
    );
};

export default CardList;
