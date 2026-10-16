import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

const Checkout: React.FC = () => {
  const [address, setAddress] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.post("/orders", { shippingAddress: address });
      alert("Porudžbina kreirana!");
      navigate("/orders");
    } catch (err: any) {
      alert(err.response?.data?.message || "Greška");
    }
  };

  return (
    <div style={{ maxWidth: "500px" }}>
      <h2>Checkout</h2>
      <form onSubmit={handleSubmit}>
        <label>Adresa za dostavu</label>
        <textarea
          value={address}
          onChange={(e) => setAddress(e.target.value)}
          required
          style={{ width: "100%", minHeight: "100px", padding: "0.5rem" }}
        />
        <button type="submit" style={{ marginTop: "1rem", padding: "0.75rem" }}>
          Potvrdi porudžbinu
        </button>
      </form>
    </div>
  );
};

export default Checkout;
