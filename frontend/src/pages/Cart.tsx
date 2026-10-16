import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api";
import type { Cart, Product } from "../types";

const CartPage: React.FC = () => {
  const [cart, setCart] = useState<Cart | null>(null);
  const [products, setProducts] = useState<Record<string, Product>>({});

  const load = async () => {
    const res = await api.get("/cart");
    setCart(res.data);
    // Load product details
    const prods: Record<string, Product> = {};
    for (const item of res.data.items) {
      const p = await api.get(`/products/${item.productId}`);
      prods[item.productId] = p.data;
    }
    setProducts(prods);
  };

  useEffect(() => {
    load();
  }, []);

  const remove = async (productId: string) => {
    await api.delete(`/cart/remove/${productId}`);
    load();
  };

  if (!cart) return <p>Učitavanje...</p>;

  const total = cart.items.reduce((sum, item) => {
    const p = products[item.productId];
    return sum + (p ? p.price * item.quantity : 0);
  }, 0);

  return (
    <div>
      <h2>Korpa</h2>
      {cart.items.length === 0 ? (
        <p>Korpa je prazna</p>
      ) : (
        <>
          {cart.items.map((item) => {
            const p = products[item.productId];
            return p ? (
              <div
                key={item.productId}
                style={{
                  display: "flex",
                  gap: "1rem",
                  marginBottom: "1rem",
                  alignItems: "center",
                }}
              >
                <img src={p.imageUrl} alt={p.name} style={{ width: "80px" }} />
                <div>
                  <h4>{p.name}</h4>
                  <p>
                    {item.quantity} x {p.price.toFixed(2)} €
                  </p>
                </div>
                <button onClick={() => remove(item.productId)}>Ukloni</button>
              </div>
            ) : null;
          })}
          <h3>Ukupno: {total.toFixed(2)} €</h3>
          <Link to="/checkout">
            <button>Nastavi na checkout</button>
          </Link>
        </>
      )}
    </div>
  );
};

export default CartPage;
