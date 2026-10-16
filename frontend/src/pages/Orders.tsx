import React, { useEffect, useState } from "react";
import api from "../services/api";
import type { Order } from "../types";

const Orders: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);

  useEffect(() => {
    api.get("/orders").then((res) => setOrders(res.data));
  }, []);

  return (
    <div>
      <h2>Moje porudžbine</h2>
      {orders.length === 0 ? (
        <p>Nema porudžbina</p>
      ) : (
        orders.map((o) => (
          <div
            key={o.id}
            style={{
              border: "1px solid #ddd",
              padding: "1rem",
              marginBottom: "1rem",
            }}
          >
            <p>ID: {o.id}</p>
            <p>Status: {o.status}</p>
            <p>Ukupno: {o.total.toFixed(2)} €</p>
            <p>Datum: {new Date(o.createdAt).toLocaleString()}</p>
            <ul>
              {o.items.map((item, i) => (
                <li key={i}>
                  {item.productName} x {item.quantity}
                </li>
              ))}
            </ul>
          </div>
        ))
      )}
    </div>
  );
};

export default Orders;
