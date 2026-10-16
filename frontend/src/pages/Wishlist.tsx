import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api";
import type { Wishlist, Product } from "../types";

const WishlistPage: React.FC = () => {
  const [wishlist, setWishlist] = useState<Wishlist | null>(null);
  const [products, setProducts] = useState<Product[]>([]);

  const load = async () => {
    const res = await api.get("/wishlist");
    setWishlist(res.data);
    const prods: Product[] = [];
    for (const id of res.data.productIds) {
      const p = await api.get(`/products/${id}`);
      prods.push(p.data);
    }
    setProducts(prods);
  };

  useEffect(() => {
    load();
  }, []);

  const remove = async (productId: string) => {
    await api.delete(`/wishlist/${productId}`);
    load();
  };

  if (!wishlist) return <p>Učitavanje...</p>;

  return (
    <div>
      <h2>Wishlist</h2>
      {products.length === 0 ? (
        <p>Wishlist je prazan</p>
      ) : (
        products.map((p) => (
          <div
            key={p.id}
            style={{
              display: "flex",
              gap: "1rem",
              marginBottom: "1rem",
              alignItems: "center",
            }}
          >
            <img src={p.imageUrl} alt={p.name} style={{ width: "80px" }} />
            <div>
              <h4>
                <Link to={`/products/${p.id}`}>{p.name}</Link>
              </h4>
              <p>{p.price.toFixed(2)} €</p>
            </div>
            <button onClick={() => remove(p.id)}>Ukloni</button>
          </div>
        ))
      )}
    </div>
  );
};

export default WishlistPage;
