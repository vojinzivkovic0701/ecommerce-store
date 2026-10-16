import { BrowserRouter as Router, Routes, Route, Link, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import Home from './pages/Home';
import ProductList from './pages/ProductList';
import ProductDetail from './pages/ProductDetail';
import Cart from './pages/Cart';
import Checkout from './pages/Checkout';
import Orders from './pages/Orders';
import Wishlist from './pages/Wishlist';
import Login from './pages/Login';
import Register from './pages/Register';
import AdminDashboard from './pages/AdminDashboard';
import './App.css';

function Navbar() {
  const { user, logout } = useAuth();
  return (
    <nav style={{ padding: '1rem', background: '#333', color: 'white', display: 'flex', gap: '1rem', alignItems: 'center' }}>
      <Link to="/" style={{ color: 'white', textDecoration: 'none', fontWeight: 'bold' }}>E-Shop</Link>
      <Link to="/products" style={{ color: 'white' }}>Proizvodi</Link>
      {user ? (
        <>
          <Link to="/cart" style={{ color: 'white' }}>Korpa</Link>
          <Link to="/wishlist" style={{ color: 'white' }}>Wishlist</Link>
          <Link to="/orders" style={{ color: 'white' }}>Porudžbine</Link>
          {user.role === 'ADMIN' && <Link to="/admin" style={{ color: 'white' }}>Admin</Link>}
          <span>Zdravo, {user.username}</span>
          <button onClick={logout} style={{ marginLeft: 'auto' }}>Odjava</button>
        </>
      ) : (
        <>
          <Link to="/login" style={{ color: 'white', marginLeft: 'auto' }}>Prijava</Link>
          <Link to="/register" style={{ color: 'white' }}>Registracija</Link>
        </>
      )}
    </nav>
  );
}

function AppRoutes() {
  const { user } = useAuth();
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/products" element={<ProductList />} />
      <Route path="/products/:id" element={<ProductDetail />} />
      <Route path="/login" element={!user ? <Login /> : <Navigate to="/" />} />
      <Route path="/register" element={!user ? <Register /> : <Navigate to="/" />} />
      <Route path="/cart" element={user ? <Cart /> : <Navigate to="/login" />} />
      <Route path="/checkout" element={user ? <Checkout /> : <Navigate to="/login" />} />
      <Route path="/orders" element={user ? <Orders /> : <Navigate to="/login" />} />
      <Route path="/wishlist" element={user ? <Wishlist /> : <Navigate to="/login" />} />
      <Route path="/admin" element={user?.role === 'ADMIN' ? <AdminDashboard /> : <Navigate to="/" />} />
    </Routes>
  );
}

function App() {
  return (
    <AuthProvider>
      <Router>
        <Navbar />
        <div style={{ padding: '1rem' }}>
          <AppRoutes />
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
