const About = () => {
    return (
      <div className="min-h-screen bg-custom-blue flex flex-col items-center justify-center text-center">
        <div className="container mx-auto px-4 flex flex-col items-center">
          <h1 className="text-4xl text-white mb-8">About</h1>
          <img src="/public/Illustration.svg" alt="Illustration" className="w-64 h-64 mb-8" />
          <h2 className="text-4xl text-custom-pink mb-4">Ausflugsplaner</h2>
          <p className="text-lg text-white">
              Geheime Seite gefunden! Glückwunsch
          </p>
        </div>
      </div>
    );
  };

  export default About;

