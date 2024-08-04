import { Outlet } from "react-router-dom";
import Navbar from "./Navbar.jsx";
import Footer from "./Footer.jsx";

const MainLayout = () => {
    return (
        <>
            <div className="circle-container">
                <div className="circle circle-1"></div>
                <div className="circle circle-2"></div>

            <Navbar />
            <div style={{ flex: '1 0 auto' }}>
                <Outlet />
            </div>
            <Footer />
            </div>
        </>
    );
};

export default MainLayout;
