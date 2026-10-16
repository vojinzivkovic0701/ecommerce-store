import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";
import type { Product, Review } from "../types";
import { useAuth } from "../context/AuthContext";

const ProductDetail: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [product, setProduct] = useState<Product | null>(null);
  const [reviews, setReviews] = useState<Review[]>([]);
  const [quantity, setQuantity] = useState(1);
  const [reviewForm, setReviewForm] = useState({ rating: 5, comment: "" });
  const { user } = useAuth();

  useEffect(() => {
    if (id) {
      api.get(`/products/${id}`).then((res) => setProduct(res.data));
      api.get(`/reviews/product/${id}`).then((res) => setReviews(res.data));
    }
  }, [id]);

  const addToCart = async () => {
    if (!user) return alert("Prijavite se");
    await api.post("/cart/add", { productId: id, quantity });
    alert("Dodato u korpu");
  };

  const addToWishlist = async () => {
    if (!user) return alert("Prijavite se");
    await api.post(`/wishlist/${id}`);
    alert("Dodato u wishlist");
  };

  const submitReview = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;
    await api.post("/reviews", {
      productId: id,
      rating: reviewForm.rating,
      comment: reviewForm.comment,
    });
    const res = await api.get(`/reviews/product/${id}`);
    setReviews(res.data);
    setReviewForm({ rating: 5, comment: "" });
  };

  if (!product) return <p>Učitavanje...</p>;

  return (
    <div>
      <h2>{product.name}</h2>
      <img
        src={product.imageUrl}
        alt={product.name}
        style={{ maxWidth: "300px" }}
      />
      <p>{product.description}</p>
      <p>Cena: {product.price.toFixed(2)} €</p>
      <p>Zaliha: {product.stock}</p>
      <p>
        Ocena: {product.averageRating.toFixed(1)} / 5 ({product.reviewCount}{" "}
        recenzija)
      </p>
      <div>
        <input
          type="number"
          min="1"
          value={quantity}
          onChange={(e) => setQuantity(Number(e.target.value))}
          style={{ width: "60px" }}
        />
        <button onClick={addToCart}>Dodaj u korpu</button>
        <button onClick={addToWishlist}>Wishlist</button>
      </div>
      <h3>Recenzije</h3>
      {reviews.map((r) => (
        <div
          key={r.id}
          style={{ borderBottom: "1px solid #eee", padding: "0.5rem 0" }}
        >
          <strong>{r.username}</strong> - {r.rating}/5
          <br />
          {r.comment}
        </div>
      ))}
      {user && (
        <form onSubmit={submitReview} style={{ marginTop: "1rem" }}>
          <h4>Ostavi recenziju</h4>
          <select
            value={reviewForm.rating}
            onChange={(e) =>
              setReviewForm({ ...reviewForm, rating: Number(e.target.value) })
            }
          >
            {[1, 2, 3, 4, 5].map((n) => (
              <option key={n} value={n}>
                {n}
              </option>
            ))}
          </select>
          <textarea
            placeholder="Komentar"
            value={reviewForm.comment}
            onChange={(e) =>
              setReviewForm({ ...reviewForm, comment: e.target.value })
            }
            style={{ width: "100%", minHeight: "80px" }}
          />
          <button type="submit">Pošalji</button>
        </form>
      )}
    </div>
  );
};

export default ProductDetail;
