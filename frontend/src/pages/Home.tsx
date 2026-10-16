import React from "react";
import { Link } from "react-router-dom";

const Home: React.FC = () => {
  return (
    <div style={{ textAlign: "center", marginTop: "2rem" }}>
      <h1>Dobrodošli u E-Shop</h1>
      <p>Vaša online prodavnica sa širokim asortimanom proizvoda.</p>
      <Link to="/products">
        <button
          style={{
            padding: "1rem 2rem",
            fontSize: "1.2rem",
            marginTop: "1rem",
          }}
        >
          Pregledaj proizvode
        </button>
      </Link>
    </div>
  );
};

export default Home;
