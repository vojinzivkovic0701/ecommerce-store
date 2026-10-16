import React, { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api";
import type { Product, Category } from "../types";

const ProductList: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [search, setSearch] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [minPrice, setMinPrice] = useState("");
  const [maxPrice, setMaxPrice] = useState("");

  const fetchProducts = async () => {
    const params: any = {};
    if (search) params.search = search;
    if (categoryId) params.categoryId = categoryId;
    if (minPrice) params.minPrice = minPrice;
    if (maxPrice) params.maxPrice = maxPrice;
    const res = await api.get("/products", { params });
    setProducts(res.data);
  };

  useEffect(() => {
    api.get("/categories").then((res) => setCategories(res.data));
    fetchProducts();
  }, []);

  useEffect(() => {
    fetchProducts();
  }, [search, categoryId, minPrice, maxPrice]);

  return (
    <div>
      <h2>Proizvodi</h2>
      <div
        style={{
          display: "flex",
          gap: "1rem",
          marginBottom: "1rem",
          flexWrap: "wrap",
        }}
      >
        <input
          placeholder="Pretraga..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          style={{ padding: "0.5rem" }}
        />
        <select
          value={categoryId}
          onChange={(e) => setCategoryId(e.target.value)}
          style={{ padding: "0.5rem" }}
        >
          <option value="">Sve kategorije</option>
          {categories.map((c) => (
            <option key={c.id} value={c.id}>
              {c.name}
            </option>
          ))}
        </select>
        <input
          type="number"
          placeholder="Min cena"
          value={minPrice}
          onChange={(e) => setMinPrice(e.target.value)}
          style={{ padding: "0.5rem", width: "100px" }}
        />
        <input
          type="number"
          placeholder="Max cena"
          value={maxPrice}
          onChange={(e) => setMaxPrice(e.target.value)}
          style={{ padding: "0.5rem", width: "100px" }}
        />
      </div>
      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fill, minmax(250px, 1fr))",
          gap: "1rem",
        }}
      >
        {products.map((p) => (
          <div
            key={p.id}
            style={{
              border: "1px solid #ddd",
              padding: "1rem",
              borderRadius: "8px",
            }}
          >
            <img
              src={p.imageUrl}
              alt={p.name}
              style={{ width: "100%", height: "150px", objectFit: "cover" }}
            />
            <h3>{p.name}</h3>
            <p>{p.price.toFixed(2)} €</p>
            <p>Zaliha: {p.stock}</p>
            <p>
              Ocena: {p.averageRating.toFixed(1)} ({p.reviewCount})
            </p>
            <Link to={`/products/${p.id}`}>Detalji</Link>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductList;
