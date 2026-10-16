import React, { useEffect, useState } from "react";
import api from "../services/api";
import type { Product, Category, Order } from "../types";

const AdminDashboard: React.FC = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [orders, setOrders] = useState<Order[]>([]);
  const [newProduct, setNewProduct] = useState({
    name: "",
    description: "",
    price: 0,
    categoryId: "",
    stock: 0,
    imageUrl: "",
  });
  const [newCategory, setNewCategory] = useState({ name: "", description: "" });

  const load = async () => {
    const [p, c, o] = await Promise.all([
      api.get("/products"),
      api.get("/categories"),
      api.get("/admin/orders"),
    ]);
    setProducts(p.data);
    setCategories(c.data);
    setOrders(o.data);
  };

  useEffect(() => {
    load();
  }, []);

  const createProduct = async (e: React.FormEvent) => {
    e.preventDefault();
    await api.post("/admin/products", newProduct);
    setNewProduct({
      name: "",
      description: "",
      price: 0,
      categoryId: "",
      stock: 0,
      imageUrl: "",
    });
    load();
  };

  const deleteProduct = async (id: string) => {
    if (confirm("Obriši proizvod?")) {
      await api.delete(`/admin/products/${id}`);
      load();
    }
  };

  const createCategory = async (e: React.FormEvent) => {
    e.preventDefault();
    await api.post("/admin/categories", newCategory);
    setNewCategory({ name: "", description: "" });
    load();
  };

  const updateOrderStatus = async (id: string, status: string) => {
    await api.put(`/admin/orders/${id}/status`, { status });
    load();
  };

  return (
    <div>
      <h2>Admin panel</h2>

      <section>
        <h3>Dodaj kategoriju</h3>
        <form onSubmit={createCategory}>
          <input
            placeholder="Naziv"
            value={newCategory.name}
            onChange={(e) =>
              setNewCategory({ ...newCategory, name: e.target.value })
            }
            required
          />
          <input
            placeholder="Opis"
            value={newCategory.description}
            onChange={(e) =>
              setNewCategory({ ...newCategory, description: e.target.value })
            }
          />
          <button type="submit">Dodaj</button>
        </form>
        <ul>
          {categories.map((c) => (
            <li key={c.id}>{c.name}</li>
          ))}
        </ul>
      </section>

      <section>
        <h3>Dodaj proizvod</h3>
        <form
          onSubmit={createProduct}
          style={{
            display: "flex",
            flexDirection: "column",
            gap: "0.5rem",
            maxWidth: "400px",
          }}
        >
          <input
            placeholder="Naziv"
            value={newProduct.name}
            onChange={(e) =>
              setNewProduct({ ...newProduct, name: e.target.value })
            }
            required
          />
          <textarea
            placeholder="Opis"
            value={newProduct.description}
            onChange={(e) =>
              setNewProduct({ ...newProduct, description: e.target.value })
            }
          />
          <input
            type="number"
            placeholder="Cena"
            value={newProduct.price}
            onChange={(e) =>
              setNewProduct({ ...newProduct, price: Number(e.target.value) })
            }
            required
          />
          <select
            value={newProduct.categoryId}
            onChange={(e) =>
              setNewProduct({ ...newProduct, categoryId: e.target.value })
            }
            required
          >
            <option value="">Izaberi kategoriju</option>
            {categories.map((c) => (
              <option key={c.id} value={c.id}>
                {c.name}
              </option>
            ))}
          </select>
          <input
            type="number"
            placeholder="Zaliha"
            value={newProduct.stock}
            onChange={(e) =>
              setNewProduct({ ...newProduct, stock: Number(e.target.value) })
            }
          />
          <input
            placeholder="Image URL"
            value={newProduct.imageUrl}
            onChange={(e) =>
              setNewProduct({ ...newProduct, imageUrl: e.target.value })
            }
          />
          <button type="submit">Dodaj proizvod</button>
        </form>
      </section>

      <section>
        <h3>Proizvodi ({products.length})</h3>
        {products.map((p) => (
          <div
            key={p.id}
            style={{
              display: "flex",
              justifyContent: "space-between",
              padding: "0.5rem",
              borderBottom: "1px solid #eee",
            }}
          >
            <span>
              {p.name} - {p.price}€ (zaliha: {p.stock})
            </span>
            <button onClick={() => deleteProduct(p.id)}>Obriši</button>
          </div>
        ))}
      </section>

      <section>
        <h3>Porudžbine</h3>
        {orders.map((o) => (
          <div
            key={o.id}
            style={{
              border: "1px solid #ddd",
              padding: "1rem",
              marginBottom: "1rem",
            }}
          >
            <p>
              ID: {o.id} | Status: {o.status} | Ukupno: {o.total.toFixed(2)}€
            </p>
            <select
              value={o.status}
              onChange={(e) => updateOrderStatus(o.id, e.target.value)}
            >
              {[
                "PENDING",
                "PROCESSING",
                "SHIPPED",
                "DELIVERED",
                "CANCELLED",
              ].map((s) => (
                <option key={s} value={s}>
                  {s}
                </option>
              ))}
            </select>
          </div>
        ))}
      </section>
    </div>
  );
};

export default AdminDashboard;
